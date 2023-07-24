package dev.ocean.sigtran.m3ua.configuration;

import dev.ocean.sigtran.m3ua.NodeType;
import lombok.Data;

import java.util.List;

@Data
public class M3UAConfiguration {
    private NodeType nodeType;
    private Integer m3uaWorkers;
    private Integer maxAsCountForRouting;
    private List<ApplicationServerProcess> applicationServerProcesses;
    private List<ApplicationServer> applicationServers;
    private List<Route> routes;
}
