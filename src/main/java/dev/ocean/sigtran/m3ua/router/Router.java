/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.router;

import dev.ocean.sigtran.m3ua.AsImpl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import dev.ocean.sigtran.m3ua.M3UAStackImpl;
import dev.ocean.sigtran.m3ua.ServiceIdentificator;
import java.io.Serializable;
import java.util.Iterator;
import org.apache.logging.log4j.*;

/**
 *
 * @author eatakishiyev
 */
public class Router implements Serializable {

    private transient final static Logger LOGGER = LogManager.getLogger(Router.class);
    private final List<Route> routeList = new ArrayList();
    private transient final M3UAStackImpl stack;

    public Router(M3UAStackImpl stack) {
        this.stack = stack;
    }

    /**
     *
     * Find route to destination on base of dpc, opc, si.
     *
     * @param dpc
     * @param opc
     * @param si
     * @return
     */
    public Route findRoute(int dpc, int opc, int si) {
        for (Route route : routeList) {
            boolean routeMatch = route.routeMatch(dpc, opc, si);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Checking route:  RoutingLabel[ DPC = " + dpc + ";OPC = " + opc + ";SI = " + si + "] " + route + " result = " + routeMatch);
            }
            if (routeMatch) {
                return route;
            }
        }
        return null;
    }

    private Route findRouteByName(String name) {
        for (Route route : routeList) {
            if (route.name.equals(name)) {
                return route;
            }
        }
        return null;
    }

    /**
     * If route exists then method will add As to existing route, else just
     * create new route with As
     *
     * @param name
     * @param dpc
     * @param opc
     * @param si
     * @return
     * @throws IOException
     */
    public Route createRouteBkp(String name, int dpc, int opc, ServiceIdentificator si) throws IOException {
        Route route = Route.createBackup(name, dpc, opc, si, stack);
        this.routeList.add(route);
        return route;
    }

    public Route createRouteLoadShare(String name, int dpc, int opc, ServiceIdentificator si) throws IOException {
        Route route = Route.createLoadShare(name, dpc, opc, si, stack);
        this.routeList.add(route);
        return route;
    }

    public Route createRouteBroadcast(String name, int dpc, int opc, ServiceIdentificator si) throws IOException {
        Route route = Route.createBroadcast(name, dpc, opc, si, stack);
        this.routeList.add(route);
        return route;
    }

    public Route createRoute(String name, int opc, int dpc, ServiceIdentificator si,
            RouteMode mode) throws IOException {
        switch (mode) {
            case BACKUP:
                return this.createRouteBkp(name, dpc, opc, si);
            case BROADCAST:
                return this.createRouteBroadcast(name, dpc, opc, si);
            case LOADSHARE:
                return this.createRouteLoadShare(name, dpc, opc, si);
        }
        return null;
    }

    public void addLinkToRoute(String name, String asName) throws Exception {
        Route route = this.findRouteByName(name);
        if (route == null) {
            throw new IOException("Route not found: " + name);
        }

        AsImpl as = stack.getM3uaManagement().getAs(asName);
        if (as == null) {
            throw new IOException("As not found: " + asName);
        }

        route.addAs(as);
    }

    /**
     * Removes only Asp from the route. Keeps route itself
     *
     * @param name
     * @param aspName
     * @throws IOException
     */
    public void removeLinkFromRoute(String name, String asName) throws IOException {

        Route route = this.findRouteByName(name);
        if (route == null) {
            throw new IOException("No route found: Name = " + name);
        }

        AsImpl as = stack.getM3uaManagement().getAs(asName);
        if (as == null) {
            throw new IOException("No As found. Name = " + asName);
        }

        route.getAsList().remove(as);
        as.removeAsStateListener(route);
    }

    /**
     * Removes route permanently.
     *
     * @param name
     * @throws IOException
     */
    public void removeRoute(String name) throws IOException {
        Route route = this.findRouteByName(name);
        if (route == null) {
            throw new IOException(String.format("Route not found: %s", name));
        }

        Iterator<AsImpl> asIter = route.getAsList().iterator();
        while (asIter.hasNext()) {
            AsImpl as = asIter.next();

            as.removeAsStateListener(route);
            asIter.remove();
        }

        this.routeList.remove(route);
    }

    /**
     * @return the destinations
     */
    public List<Route> getRoutes() {
        return routeList;
    }

}
