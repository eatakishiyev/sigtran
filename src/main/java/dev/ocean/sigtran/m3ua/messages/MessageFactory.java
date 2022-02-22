/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua.messages;

import java.io.IOException;
import dev.ocean.sigtran.m3ua.Message;
import dev.ocean.sigtran.m3ua.MessageClass;
import static dev.ocean.sigtran.m3ua.MessageClass.*;
import dev.ocean.sigtran.m3ua.MessageType;
import static dev.ocean.sigtran.m3ua.MessageType.*;
import dev.ocean.sigtran.m3ua.exceptions.InvalidVersionException;
import dev.ocean.sigtran.m3ua.exceptions.UnsupportedMessageClassException;
import dev.ocean.sigtran.m3ua.exceptions.UnsupportedMessageTypeException;
import dev.ocean.sigtran.m3ua.io.M3UAMessageByteArrayInputStream;
import dev.ocean.sigtran.m3ua.io.M3UAParameterByteArrayInputStream;
import dev.ocean.sigtran.m3ua.messages.aspsm.AspDown;
import dev.ocean.sigtran.m3ua.messages.aspsm.AspDownAck;
import dev.ocean.sigtran.m3ua.messages.aspsm.AspUp;
import dev.ocean.sigtran.m3ua.messages.aspsm.AspUpAck;
import dev.ocean.sigtran.m3ua.messages.aspsm.HeartBeat;
import dev.ocean.sigtran.m3ua.messages.aspsm.HeartbeatAck;
import dev.ocean.sigtran.m3ua.messages.asptm.ASPActive;
import dev.ocean.sigtran.m3ua.messages.asptm.ASPActiveAck;
import dev.ocean.sigtran.m3ua.messages.asptm.ASPInactive;
import dev.ocean.sigtran.m3ua.messages.asptm.ASPInactiveAck;
import dev.ocean.sigtran.m3ua.messages.mgmt.ErrorMessage;
import dev.ocean.sigtran.m3ua.messages.mgmt.Notify;
import dev.ocean.sigtran.m3ua.messages.rkm.DerigstrationRequest;
import dev.ocean.sigtran.m3ua.messages.rkm.DerigstrationResponse;
import dev.ocean.sigtran.m3ua.messages.rkm.RegistrationRequest;
import dev.ocean.sigtran.m3ua.messages.rkm.RegistrationResponse;
import dev.ocean.sigtran.m3ua.messages.ssnm.DestinationAvailable;
import dev.ocean.sigtran.m3ua.messages.ssnm.DestinationRestricted;
import dev.ocean.sigtran.m3ua.messages.ssnm.DestinationStateAudit;
import dev.ocean.sigtran.m3ua.messages.ssnm.DestinationUnavailable;
import dev.ocean.sigtran.m3ua.messages.ssnm.DestinationUserPartUnavailable;
import dev.ocean.sigtran.m3ua.messages.ssnm.SignallingCongestion;
import dev.ocean.sigtran.m3ua.messages.transfer.PayloadData;

/**
 *
 * @author root
 */
public class MessageFactory {

    private MessageFactory() {
    }

    public static Message createMessage(byte[] data) throws InvalidVersionException, UnsupportedMessageClassException, IOException, UnsupportedMessageTypeException {

        M3UAMessageByteArrayInputStream mbais = new M3UAMessageByteArrayInputStream(data);
        int version = mbais.readVersion();
        if (version > 1) {
            throw new InvalidVersionException(String.format("Unsupported version is received. Version=%d", version));
        }

        int reserved = mbais.readReserved();

        MessageClass messageClass = mbais.readMessageClass();
        if (messageClass == MessageClass.UNKNOWN) {
            byte[] messageFragment = new byte[40];
            System.arraycopy(data, 0, messageFragment, 0, 40);
            throw new UnsupportedMessageClassException("Unsupported message class received.", messageFragment);
        }

        MessageType messageType = mbais.readMessageType();
        if (messageType == MessageType.UNKNOWN) {
            byte[] messageFragment = new byte[40];
            System.arraycopy(data, 0, messageFragment, 0, 40);
            throw new UnsupportedMessageTypeException("Unsupported message type received.", messageFragment);
        }

        int length = mbais.readMessageLength();
        M3UAParameterByteArrayInputStream messageContent = mbais.readMessageContet();

        switch (messageClass) {
            case ASPSM:
                switch (messageType) {
                    case ASPDN:
                        AspDown aSPDown = new AspDown();
                        aSPDown.decode(messageContent);
                        return aSPDown;
                    case ASPDN_ACK:
                        AspDownAck aSPDownAck = new AspDownAck();
                        aSPDownAck.decode(messageContent);
                        return aSPDownAck;
                    case ASPUP:
                        AspUp aSPUp = new AspUp();
                        aSPUp.decode(messageContent);
                        return aSPUp;
                    case ASPUP_ACK:
                        AspUpAck aSPUpAck = new AspUpAck();
                        aSPUpAck.decode(messageContent);
                        return aSPUpAck;
                    case BEAT:
                        HeartBeat heartbeat = new HeartBeat();
                        heartbeat.decode(messageContent);
                        return heartbeat;
                    case BEAT_ACK:
                        HeartbeatAck heartbeatAck = new HeartbeatAck();
                        heartbeatAck.decode(messageContent);
                        return heartbeatAck;
                }
            case ASPTM:
                switch (messageType) {
                    case ASPAC:
                        ASPActive aSPActive = new ASPActive();
                        aSPActive.decode(messageContent);
                        return aSPActive;
                    case ASPAC_ACK:
                        ASPActiveAck aSPActiveAck = new ASPActiveAck();
                        aSPActiveAck.decode(messageContent);
                        return aSPActiveAck;
                    case ASPIA:
                        ASPInactive aSPInactive = new ASPInactive();
                        aSPInactive.decode(messageContent);
                        return aSPInactive;
                    case ASPIA_ACK:
                        ASPInactiveAck aSPInactiveAck = new ASPInactiveAck();
                        aSPInactiveAck.decode(messageContent);
                        return aSPInactiveAck;
                }
            case MGMT:
                switch (messageType) {
                    case ERR:
                        ErrorMessage error = new ErrorMessage();
                        error.decode(messageContent);
                        return error;
                    case NTFY:
                        Notify notify = new Notify();
                        notify.decode(messageContent);
                        return notify;
                }
            case SSNM:
                switch (messageType) {
                    case DAVA:
                        DestinationAvailable destinationAvailable = new DestinationAvailable();
                        destinationAvailable.decode(messageContent);
                        return destinationAvailable;
                    case DRST:
                        DestinationRestricted destinationRestricted = new DestinationRestricted();
                        destinationRestricted.decode(messageContent);
                        return destinationRestricted;
                    case DAUD:
                        DestinationStateAudit destinationStateAudit = new DestinationStateAudit();
                        destinationStateAudit.decode(messageContent);
                        return destinationStateAudit;
                    case SCON:
                        SignallingCongestion signallingCongestion = new SignallingCongestion();
                        signallingCongestion.decode(messageContent);
                        return signallingCongestion;
                    case DUPU:
                        DestinationUserPartUnavailable destinationUserPartUnavailable = new DestinationUserPartUnavailable();
                        destinationUserPartUnavailable.decode(messageContent);
                        return destinationUserPartUnavailable;
                    case DUNA:
                        DestinationUnavailable destinationUnavailable = new DestinationUnavailable();
                        destinationUnavailable.decode(messageContent);
                        return destinationUnavailable;
                }
            case TRANSFER_MESSAGE:
                switch (messageType) {
                    case DATA:
                        PayloadData payloadData = new PayloadData();
                        payloadData.decode(messageContent);
                        return payloadData;
                }

        }
        return null;
    }

    public static Message createMessage(MessageClass messageClass, MessageType messageType) {
        switch (messageClass) {
            case ASPSM:
                switch (messageType) {
                    case ASPUP:
                        return new AspUp();
                    case ASPDN:
                        return new AspDown();
                    case BEAT:
                        return new HeartBeat();
                    case ASPUP_ACK:
                        return new AspUpAck();
                    case ASPDN_ACK:
                        return new AspDownAck();
                    case BEAT_ACK:
                        return new HeartbeatAck();
                }
                break;
            case ASPTM:
                switch (messageType) {
                    case ASPAC:
                        return new ASPActive();
                    case ASPIA:
                        return new ASPInactive();
                    case ASPAC_ACK:
                        return new ASPActiveAck();
                    case ASPIA_ACK:
                        return new ASPInactiveAck();
                }
                break;
            case MGMT:
                switch (messageType) {
                    case ERR:
                        return new ErrorMessage();
                    case NTFY:
                        return new Notify();
                }
                break;
            case RKM:
                switch (messageType) {
                    case REG_REQ:
                        return new RegistrationRequest();
                    case REG_RSP:
                        return new RegistrationResponse();
                    case DEREG_REQ:
                        return new DerigstrationRequest();
                    case DEREG_RSP:
                        return new DerigstrationResponse();
                }
                break;
            case SSNM:
                switch (messageType) {
                    case DUNA:
                        return new DestinationUnavailable();
                    case DAVA:
                        return new DestinationAvailable();
                    case DAUD:
                        return new DestinationStateAudit();
                    case SCON:
                        return new SignallingCongestion();
                    case DUPU:
                        return new DestinationUserPartUnavailable();
                    case DRST:
                        return new DestinationRestricted();
                }
                break;
            case TRANSFER_MESSAGE:
                return new PayloadData();
        }
        return null;
    }
}
