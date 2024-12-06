/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.sctp.SctpProvider;
import azrc.az.sigtran.m3ua.configuration.M3UAConfiguration;
import azrc.az.sigtran.m3ua.parameters.Cause;
import azrc.az.sigtran.m3ua.router.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.management.*;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author eatakishiyev
 */
public class M3UAStackImpl implements M3UAStack {

    public static final Logger logger = LoggerFactory.getLogger(M3UAStackImpl.class);

    private M3UAProviderImpl m3uaProvider = null;
    //
    protected int maxSLS = 255;

    private M3UALayerManagement m3uaManagement;

    private final Map<ServiceIdentificator, M3UAUser> users = new HashMap<>();

    protected final List<As> configuredAsList = new ArrayList<>();
    protected final List<Asp> aspList = new ArrayList<>();
    protected Router router;

    private final String mbeanName = "dev.ocean.sigtran.management:type=Management,name=M3UA";

    private String infoString;
    protected NodeType nodeType = NodeType.ASP;

    protected SctpProvider sctpProvider;

    protected int workersCount;

    private static M3UAStackImpl instance;
    private int maxAsCountForRouting = 2;

    protected int asSelectMask = 0x80; //1000 0000
    protected int aspSelectMask = 0x7F;//0111 1111


    public M3UAStackImpl(M3UAConfiguration configuration) throws IOException, MalformedObjectNameException, InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        logger.info("==========================| Starting M3UA Stack |==========================");
        this.m3uaProvider = new M3UAProviderImpl(this);
        logger.info("[M3UA]:Starting M3UALayerManagement...");
        this.m3uaManagement = new M3UALayerManagement(this);
        this.router = new Router(this);
        try {
            logger.info("[M3UA]:Starting SCTP Layer...");
            sctpProvider = SctpProvider.getInstance();

            this.m3uaManagement.loadConfiguration(configuration);
        } catch (Throwable ex) {
            logger.error("Error occured while registering M3UA mbean: ", ex);
        }

        logger.info(String.format("[M3UA]:Registering MBean. %s", this.mbeanName));
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        mbeanServer.registerMBean(this.m3uaManagement, new ObjectName(mbeanName));

        logger.info("==========================| M3UA Stack started |==========================");
    }

    @Override
    public void fireMTPPause(int pointCode) {
        users.values().forEach((user) -> {
            user.onMTPPause(pointCode);
        });
    }

    @Override
    public void fireMTPResume(int pointCode) {
        users.values().forEach((user) -> {
            user.onMTPResume(pointCode);
        });
    }

    @Override
    public void fireMTPStatus(int dpc, int congestionLevel) {
        users.values().forEach((user) -> {
            user.onMTPStatus(dpc, congestionLevel);
        });
    }

    @Override
    public void fireMTPStatus(int dpc, Cause cause, ServiceIdentificator userId) {
        users.values().stream().filter((user) -> (user.getServiceIdentificator().value() != userId.value())).forEachOrdered((user) -> {
            user.onMTPStatus(dpc, cause);
        });
    }

    @Override
    public void addUser(M3UAUser user) {
        users.put(user.getServiceIdentificator(), user);
    }

    @Override
    public M3UAUser getUser(ServiceIdentificator si) {
        return users.get(si);
    }

    /**
     * @return the maxSLS
     */
    public int getMaxSLS() {
        return maxSLS;
    }

    /**
     * @param maxSLS the maxSLS to set
     */
    public void setMaxSLS(int maxSLS) {
        this.maxSLS = maxSLS;
    }

    /**
     * @return the m3uaManagement
     */
    public M3UALayerManagement getM3uaManagement() {
        return m3uaManagement;
    }

    /**
     * @return the m3uaProvider
     */
    @Override
    public M3UAProvider getM3uaProvider() {
        return m3uaProvider;
    }

    /**
     * @return the mbeanName
     */
    public String getMbeanName() {
        return mbeanName;
    }

    /**
     * @return the infoString
     */
    public String getInfoString() {
        return infoString;
    }

    /**
     * @param infoString the infoString to set
     */
    public void setInfoString(String infoString) {
        this.infoString = infoString;
    }

    @Override
    public NodeType getNodeType() {
        return nodeType;
    }

    @Override
    public void setNodeType(NodeType nodeType) {
        this.nodeType = nodeType;
    }

    @Override
    public int getWorkersCount() {
        return this.workersCount;
    }

    @Override
    public void setWorkersCount(int workersCount) {
        this.workersCount = workersCount;
    }


    public As findAs(String name) {
        for (As as : configuredAsList) {
            if (as.getName().equals(name)) {
                return as;
            }
        }
        return null;
    }

    public Asp findAsp(String name) {
        for (Asp asp : aspList) {
            if (asp.getName().equals(name)) {
                return asp;
            }
        }
        return null;
    }

    public int getMaxAsCountForRouting() {
        return maxAsCountForRouting;
    }

    public void setMaxAsCountForRouting(int maxAsCountForRouting) {
        this.maxAsCountForRouting = maxAsCountForRouting;

        switch (maxAsCountForRouting) {
            case 1:
            case 2:
                this.asSelectMask = 0x80;
                this.aspSelectMask = 0x7F;
                break;
            case 3:
            case 4:
                this.asSelectMask = 0xC0;
                this.aspSelectMask = 0x3F;
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                this.asSelectMask = 0xE0;
                this.aspSelectMask = 0x1F;
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
                this.asSelectMask = 0xF0;
                this.aspSelectMask = 0x0F;
                break;
            default:
                break;
        }
    }

    public int getAsSelectMask() {
        return asSelectMask;
    }

    public int getAspSelectMask() {
        return aspSelectMask;
    }

}
