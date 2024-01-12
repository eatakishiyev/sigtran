package azrc.az.sigtran.sccp.general.configuration;

import lombok.Data;

import java.util.List;

@Data
public class SCCPConfiguration {
    private Integer minNotSegmentedMessageSize;
    private Integer maxMessageSize;
    private Boolean removeSpc;
    private Integer hopCounter;
    private Integer reassemblyTimer;
    private Integer sstTimerMin;
    private Integer sstTimerMax;
    private Integer sstTimerIncreaseBy;
    private Boolean sstIgnore;
    private LocalPointCode localPointCode;
    private List<RemoteSignallingPoint> remoteSignallingPoints;
    private List<MtpSap> mtpSaps;
    private List<SCCPEntitySet> sccpEntitySets;
    private List<Translator> translators;
}
