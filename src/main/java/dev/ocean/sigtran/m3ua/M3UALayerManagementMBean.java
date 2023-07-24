/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import dev.ocean.sigtran.m3ua.configuration.M3UAConfiguration;
import dev.ocean.sigtran.m3ua.parameters.TrafficMode;
import dev.ocean.sigtran.m3ua.router.Route;
import dev.ocean.sigtran.m3ua.router.RouteMode;
import java.io.IOException;
import java.util.List;

/**
 * ASP mode Layer Management
 *
 * @author eatakishiyev
 */
public interface M3UALayerManagementMBean {

    public String showAsp(String name) throws Exception;

    public String showAs(String name) throws Exception;

    public void createAs(String name,
            int routingContext, boolean sendRc, int networkAppearance) throws Exception;

    public void createAsp(String name, String localHost, String additionalAddress,
            int localPort, String peerHost, int peerPort, int inStreams,
            int outStreams, boolean autreconnect, boolean noDelay, int soSndBuf,
            int soRcvBuf, LinkType mode, int aspUpTimeOut, int aspDownTimeOut,
            int aspActiveTimeOut, int aspInactiveTimeOut, int workersCount,
            boolean autoStart, Integer aspIdentifier, TrafficMode trafficMode) throws Exception;

    public List<AspImpl> getAspList();

    public List<AsImpl> getAsList();

    public List<Route> getRoutes();

    public void removeAs(String name) throws Exception;

    public Route createRoute(String name, int dpc, int opc, ServiceIdentificator si, RouteMode mode) throws Exception;

    public Route createRouteBkp(String name, int dpc, int opc, ServiceIdentificator si) throws Exception;

    public Route createRouteLoadShare(String name, int dpc, int opc, ServiceIdentificator si) throws IOException;

    public Route createRouteBroadcast(String name, int dpc, int opc, ServiceIdentificator si) throws IOException;

    public void addLinkToRoute(String name, String aspName) throws Exception;

    public void removeLinkFromRoute(String name, String aspName) throws IOException;

    public void removeRoute(String name) throws IOException;

    public void startSgp(String aspName) throws Exception;

    public void stopSgp(String aspName) throws Exception;

    public void inactivateAs(String as, String sgp) throws Exception;

    public void removeAsp(String name) throws Exception;

    public void removeAspFromAs(String aspName, String asName) throws Exception;

    public void addAspToAs(String aspName, String asName) throws Exception;

    public int AsCount();

    public As getNullRcAs();

    public As getAsByRc(int rc);

    public void loadConfiguration(M3UAConfiguration m3UAConfiguration) throws Exception;

    public AspImpl getAsp(String name);

    public AsImpl getAs(String name);
    
    
}
