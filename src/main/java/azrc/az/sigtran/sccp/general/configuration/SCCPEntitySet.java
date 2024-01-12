package azrc.az.sigtran.sccp.general.configuration;

import azrc.az.sigtran.sccp.address.SubSystemNumber;
import azrc.az.sigtran.sccp.gtt.SccpEntitySet;
import lombok.Data;

@Data
public class SCCPEntitySet {
    private String name;
    private SubSystemNumber ssn;
    private SccpEntitySet.Mode mode;
    private String[] mtpSaps;
}
