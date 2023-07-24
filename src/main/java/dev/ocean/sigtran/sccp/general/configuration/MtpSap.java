package dev.ocean.sigtran.sccp.general.configuration;

import dev.ocean.sigtran.m3ua.NetworkIndicator;
import dev.ocean.sigtran.sccp.messages.MessageType;
import lombok.Data;

@Data
public class MtpSap {
    private String name;
    private Integer dpc;
    private Integer opc;
    private NetworkIndicator ni;
    private MessageType targetMessageType;
}
