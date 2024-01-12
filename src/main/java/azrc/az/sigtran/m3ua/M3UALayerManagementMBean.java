/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import azrc.az.sigtran.m3ua.parameters.TrafficMode;
import azrc.az.sigtran.m3ua.router.Route;
import azrc.az.sigtran.m3ua.router.RouteMode;
import azrc.az.sigtran.m3ua.configuration.M3UAConfiguration;

import java.io.IOException;
import java.util.List;

/**
 * ASP mode Layer Management
 *
 * @author eatakishiyev
 */
public interface M3UALayerManagementMBean {

     String showAsp(String name) throws Exception;

     String showAs(String name) throws Exception;

     void createAs(String name,
                         int routingContext, boolean sendRc, int networkAppearance) throws Exception;

     void createAsp(String name, String localHost, String additionalAddress,
                          int localPort, String peerHost, int peerPort, int inStreams,
                          int outStreams, boolean autreconnect, boolean noDelay, int soSndBuf,
                          int soRcvBuf, LinkType mode, int aspUpTimeOut, int aspDownTimeOut,
                          int aspActiveTimeOut, int aspInactiveTimeOut, int workersCount,
                          boolean autoStart, Integer aspIdentifier, TrafficMode trafficMode) throws Exception;

    List<Asp> getAspList();

    List<As> getAsList();

    List<Route> getRoutes();

    void removeAs(String name) throws Exception;

    Route createRoute(String name, int dpc, int opc, ServiceIdentificator si, RouteMode mode) throws Exception;

    Route createRouteBkp(String name, int dpc, int opc, ServiceIdentificator si) throws Exception;

    Route createRouteLoadShare(String name, int dpc, int opc, ServiceIdentificator si) throws IOException;

    Route createRouteBroadcast(String name, int dpc, int opc, ServiceIdentificator si) throws IOException;

    void addLinkToRoute(String name, String aspName) throws Exception;

    void removeLinkFromRoute(String name, String aspName) throws IOException;

    void removeRoute(String name) throws IOException;

    void startSgp(String aspName) throws Exception;

    void stopSgp(String aspName) throws Exception;

    void inactivateAs(String as, String sgp) throws Exception;

    void removeAsp(String name) throws Exception;

    void removeAspFromAs(String aspName, String asName) throws Exception;

    void addAspToAs(String aspName, String asName) throws Exception;

    int AsCount();

    As getNullRcAs();

    As getAsByRc(int rc);

    void loadConfiguration(M3UAConfiguration m3UAConfiguration) throws Exception;

    Asp getAsp(String name);

    As getAs(String name);


}
