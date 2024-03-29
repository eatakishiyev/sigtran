package azrc.az.sigtran.m3ua.configuration;

import azrc.az.sigtran.m3ua.ServiceIdentificator;
import azrc.az.sigtran.m3ua.router.RouteMode;
import lombok.Data;

import java.util.List;

@Data
public class Route {
    private String name;
    private Integer opc;
    private Integer dpc;
    private ServiceIdentificator si;
    private RouteMode mode;
    private List<String> applicationServers;
}
