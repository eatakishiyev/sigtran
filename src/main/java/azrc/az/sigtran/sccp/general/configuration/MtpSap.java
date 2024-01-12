package azrc.az.sigtran.sccp.general.configuration;

import azrc.az.sigtran.m3ua.NetworkIndicator;
import azrc.az.sigtran.sccp.messages.MessageType;
import lombok.Data;

@Data
public class MtpSap {
    private String name;
    private Integer dpc;
    private Integer opc;
    private NetworkIndicator ni;
    private MessageType targetMessageType;
}
