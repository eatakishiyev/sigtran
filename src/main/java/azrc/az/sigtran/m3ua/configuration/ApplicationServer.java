package azrc.az.sigtran.m3ua.configuration;

import lombok.Data;

import java.util.List;

@Data
public class ApplicationServer {
    private String name;
    private Integer routingContext;
    private Boolean sendRoutingContext;
    private Integer networkAppearance;
    private List<String> applicationServerProcesses;
}
