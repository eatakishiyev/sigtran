package dev.ocean.sigtran.sccp.general.configuration;

import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import lombok.Data;

@Data
public class RemoteSignallingPoint {
    private String name;
    private Integer spc;
    private Boolean concerned;
    private SubSystemNumber[] remoteSubSystems;
}
