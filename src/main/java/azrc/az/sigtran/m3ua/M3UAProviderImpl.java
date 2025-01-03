/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.sigtran.m3ua.router.Route;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class M3UAProviderImpl implements M3UAProvider {

    private transient static final Logger LOGGER = LoggerFactory.getLogger(M3UAProviderImpl.class);
    protected transient M3UAStackImpl m3uaStack;

    protected M3UAProviderImpl(M3UAStackImpl m3uaStack) throws IOException {
        this.m3uaStack = m3uaStack;
    }

    @Override
    public void send(MTPTransferMessage mtpTransferMessage) throws Exception {
        int dpc = mtpTransferMessage.getDpc();
        int opc = mtpTransferMessage.getOpc();
        ServiceIdentificator si = mtpTransferMessage.getSi();

        Route route = this.m3uaStack.router.findRoute(dpc, opc, si.value());

        if (route == null) {
            LOGGER.error(String.format("No route found. DPC = %d OPC = %d SI = %s", dpc, opc, si));
            return;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Route found: " + route);
        }

        route.routeMessage(mtpTransferMessage);
    }

    @Override
    public void addUser(M3UAUser user) {
        m3uaStack.addUser(user);
    }

    @Override
    public int getMaxSls() {
        return this.m3uaStack.getMaxSLS();
    }
}
