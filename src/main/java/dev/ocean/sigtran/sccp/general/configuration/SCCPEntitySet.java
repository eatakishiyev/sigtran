package dev.ocean.sigtran.sccp.general.configuration;

import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import dev.ocean.sigtran.sccp.gtt.SccpEntitySet;
import lombok.Data;

@Data
public class SCCPEntitySet {
    private String name;
    private SubSystemNumber ssn;
    private SccpEntitySet.Mode mode;
    private String[] mtpSaps;
}
