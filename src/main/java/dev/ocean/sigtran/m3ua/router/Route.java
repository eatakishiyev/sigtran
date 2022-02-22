/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.router;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import dev.ocean.sigtran.m3ua.AsImpl;
import dev.ocean.sigtran.m3ua.AsStateListener;
import dev.ocean.sigtran.m3ua.M3UAStackImpl;
import dev.ocean.sigtran.m3ua.MTPTransferMessage;
import dev.ocean.sigtran.m3ua.ServiceIdentificator;
import dev.ocean.sigtran.m3ua.messages.transfer.PayloadData;
import dev.ocean.sigtran.m3ua.parameters.ProtocolData;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.*;

/**
 * The ASP must choose an SGP to direct a message to the SS7 network. This is
 * accomplished by observing the Destination Point Code (and possibly other
 * elements of the outgoing message, such as the SLS value). The ASP must also
 * take into account whether the related Routing Context is active or not (see
 * Section 4.3.4.3). Implementation Note: Where more than one route (or SGP) is
 * possible for routing to the SS7 network, the ASP could, for example, maintain
 * a dynamic table of available SGP routes for the SS7 destinations, taking into
 * account the SS7 destination availability/restricted/congestion status
 * received from the SGP(s), the availability status of the individual SGPs, and
 * configuration changes and failover mechanisms. There is, however, no M3UA
 * messaging to manage the status of an SGP (e.g., SGPUp/Down/Active/Inactive
 * messaging). Whenever an SCTP association to an SGP exists, the SGP is
 * assumed*- to be ready for the purposes of responding to M3UA ASPSM messages
 * (refer to Section 3).
 *
 * @author eatakishiyev
 */
public class Route implements AsStateListener {

    public static int WILDCARD = -1;

    private transient final static Logger LOGGER = LogManager.getLogger(Route.class);
//    
    private final List<AsImpl> applicationServers = new ArrayList();
    protected final String name;
    protected RouteMode mode = RouteMode.BACKUP;
    protected LoadShareAlgo loadShareAlgo = LoadShareAlgo.ROUND_ROBIN;
//    
    protected int dpc = WILDCARD;
    protected int opc = WILDCARD;
    protected ServiceIdentificator si;

    private transient final Random randomizer = new Random();

    private transient M3UAStackImpl stack;
    private transient final AtomicInteger activePathCount = new AtomicInteger(0);

    public static Route createLoadShare(String name, int dpc, int opc,
            ServiceIdentificator si, M3UAStackImpl stack) {
        Route route = new Route(name, dpc, stack);
        route.opc = opc;
        route.si = si;
        route.mode = RouteMode.LOADSHARE;
        route.stack = stack;
        return route;
    }

    public static Route createBackup(String name, int dpc, int opc,
            ServiceIdentificator si, M3UAStackImpl stack) {
        Route route = new Route(name, dpc, stack);
        route.opc = opc;
        route.si = si;
        route.mode = RouteMode.BACKUP;
        return route;
    }

    public static Route createBroadcast(String name, int dpc, int opc,
            ServiceIdentificator si, M3UAStackImpl stack) {
        Route route = new Route(name, dpc, stack);
        route.opc = opc;
        route.si = si;
        route.mode = RouteMode.BROADCAST;
        return route;
    }

    private Route(String name, int dpc, RouteMode mode, M3UAStackImpl stack) {
        this.name = name;
        this.dpc = dpc;
        this.mode = mode;
        this.stack = stack;
    }

    private Route(String name, Integer dpc, M3UAStackImpl stack) {
        this.name = name;
        this.dpc = dpc;
        this.stack = stack;
    }

    public void routeMessage(MTPTransferMessage mtpMessage) throws IOException {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Route message. Mode :" + this.mode + mtpMessage);
        }

        ProtocolData protocolData = new ProtocolData(mtpMessage);

        PayloadData payloadData = new PayloadData();
        payloadData.setProtocolData(protocolData);

        this.routeMessage(payloadData);
    }

    public void routeMessage(PayloadData payloadData) throws IOException {
        switch (this.mode) {
            case BACKUP:
                this.processBackup(payloadData);
                break;
            case LOADSHARE:
                this.processLoadshare(payloadData);
                break;
            case BROADCAST:
                this.processBroadcast(payloadData);
                break;
        }
    }

    private void processBackup(PayloadData payload) throws IOException {
        for (AsImpl as : applicationServers) {
            if (as.send(payload)) {
                return;
            }
        }
        LOGGER.error(String.format("No active Asp found to route message. DPC = %d "
                + "OPC = %s SI = %s", dpc, opc, si));
    }

    private void processLoadshare(PayloadData payload) throws IOException {
        int asIndex = payload.getProtocolData().getSls() & stack.getAsSelectMask();
        asIndex = asIndex % applicationServers.size();

        AsImpl as = applicationServers.get(asIndex);
        if (as.send(payload)) {
            return;
        }

        for (int i = 0; i < applicationServers.size(); i++) {
            as = applicationServers.get(i);
            if (as.send(payload)) {
                return;
            }
        }
    }

    private void processBroadcast(PayloadData payload) {
        applicationServers.forEach((as) -> {
            as.send(payload);
        });
    }

    protected boolean routeMatch(int dpc, int opc, int si) {
        return (this.dpc == dpc) && (this.opc < 0 ? true : this.opc == opc)
                && (this.si == null ? true : this.si.value() == si);
    }

    public int getOpc() {
        return opc;
    }

    public void setOpc(int opc) {
        this.opc = opc;
    }

    /**
     * @return the dpc
     */
    public int getDpc() {
        return dpc;
    }

    /**
     * @param dpc the dpc to set
     */
    public void setDpc(int dpc) {
        this.dpc = dpc;
    }

    public RouteMode getMode() {
        return mode;
    }

    public void setMode(RouteMode mode) {
        this.mode = mode;
    }

    public LoadShareAlgo getLoadShareAlgo() {
        return loadShareAlgo;
    }

    public void setLoadShareAlgo(LoadShareAlgo loadShareAlgo) {
        this.loadShareAlgo = loadShareAlgo;
    }

    public List<AsImpl> getAsList() {
        return applicationServers;
    }

    @Override
    public void onAsActive(int rc) {
        stack.fireMTPResume(dpc);
    }

    @Override
    public void onAsDown(int rc) {
        stack.fireMTPPause(dpc);
    }

    @Override
    public void addAs(AsImpl as) throws Exception {
        if (applicationServers.contains(as)) {
            throw new Exception("Application Server already exists in this route. As = " + as.getName() + ". Route = " + name);
        }

        this.applicationServers.add(as);
        as.addAsStateListener(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    public ServiceIdentificator getSi() {
        return si;
    }

    public void setSi(ServiceIdentificator si) {
        this.si = si;
    }

    @Override
    public String toString() {
        return String.format("Route:[Name = %s; DPC = %s; OPC = %s; SI = %s;]",
                this.name, this.dpc, this.opc, this.si);
    }

    @Override
    public boolean equals(Object other) {
        if (other != null && other instanceof Route) {
            Route route = (Route) other;
            return route.getName().equals(this.getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
