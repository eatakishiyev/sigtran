package azrc.az.sigtran.sccp.general.configuration;

import azrc.az.sigtran.m3ua.NetworkIndicator;
import lombok.Data;

@Data
public class LocalPointCode {
    private String name;
    private NetworkIndicator networkIndicator;
    private Integer spc;
}
