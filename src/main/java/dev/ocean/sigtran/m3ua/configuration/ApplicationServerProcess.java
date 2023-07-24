package dev.ocean.sigtran.m3ua.configuration;

import dev.ocean.sigtran.m3ua.LinkType;
import dev.ocean.sigtran.m3ua.parameters.TrafficMode;
import dev.ocean.sigtran.m3ua.parameters.TrafficModeType;
import lombok.Data;

@Data
public class ApplicationServerProcess {
    private String name;
    private Integer aspId;
    private String bindHost1;
    private String bindHost2;
    private Integer bindPort;
    private String remoteHost;
    private Integer remotePort;
    private Integer streams;
    private LinkType linkType;
    private Boolean noDelay;
    private Integer soSndBuf;
    private Integer soRcvBuf;
    private Boolean autoReconnect;
    private Integer aspUpTimeOut;
    private Integer aspDownTimeOut;
    private Integer aspActiveTimeOut;
    private Integer aspInactiveTimeOut;
    private TrafficMode trafficMode;
    private Integer workers;
    private Boolean autoStart;
}
