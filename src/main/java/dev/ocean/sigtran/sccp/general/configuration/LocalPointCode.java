package dev.ocean.sigtran.sccp.general.configuration;

import dev.ocean.sigtran.m3ua.NetworkIndicator;
import lombok.Data;

@Data
public class LocalPointCode {
    private String name;
    private NetworkIndicator networkIndicator;
    private Integer spc;
}
