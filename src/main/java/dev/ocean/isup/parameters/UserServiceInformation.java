/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.isup.parameters;

import dev.ocean.isup.enums.CodingStandard;
import dev.ocean.isup.enums.InformationTransferCapability;
import dev.ocean.isup.enums.InformationTransferRate;
import dev.ocean.isup.enums.IntermediateRate;
import dev.ocean.isup.enums.LogicalLinkIdentifierNegotiation;
import dev.ocean.isup.enums.ModeOfOperation;
import dev.ocean.isup.enums.ModemType;
import dev.ocean.isup.enums.NumberOfDataBits;
import dev.ocean.isup.enums.NumberOfStopBits;
import dev.ocean.isup.enums.Parity;
import dev.ocean.isup.enums.TransferMode;
import dev.ocean.isup.enums.UserInformationLayer1Protocol;
import dev.ocean.isup.enums.UserInformationLayer2Protocol;
import dev.ocean.isup.enums.UserInformationLayer3Protocol;
import dev.ocean.isup.enums.UserRate;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class UserServiceInformation implements IsupParameter {

    private final int LAYER1_IDENTITY = 1;
    private final int LAYER2_IDENTITY = 2;
    private final int LAYER3_IDENTITY = 3;

    //1st octet data
    CodingStandard codingStandard;
    InformationTransferCapability informationTransferCapability;

    //2nd octet data
    TransferMode transferMode;
    InformationTransferRate informationTransferRate;

    //2a octet data. Octet 2a required if octet 2 indicates multirate; otherwise, ot shall not be present.
    private int rateMultiplier = -1;

    //3rd octet.
    private UserInformationLayer1Protocol userInformationLayer1Protocol;
//    private byte[] userInformationLayer1ProtocolExtension;
    //3rd octet extension, first octet
    private Boolean asynchronousData;// asynchronousData = 1
    private Boolean inBandNegotiationPossible;//inBandNegotiationPossible = 1
    private UserRate userRate;
    //3rd octet extension, second octet
    private IntermediateRate intermediateRate;
    private Boolean requiredToSendDataWithNetworkIndependentClock;
    private Boolean canAcceptDataWithNetworkIndependentClock;
    private Boolean requiredToSendDataWithFlowControlMechanism;
    private Boolean canAcceptDataWithFlowControlMechanism;
    //3rd octet extension, 3rd octet
    private Boolean rateAdaptationHeaderIncluded;
    private Boolean multipleFrameEstablishmentSupported;
    private ModeOfOperation mode;
    private LogicalLinkIdentifierNegotiation logicalLinkIdentifierNegotiation;
    private Boolean messageOriginatorAssignorOnly;
    private Boolean negotiationIsDoneInBandUsingLogicalLinkZero;
    //3rd octet extension, 4th octet
    private NumberOfStopBits numberOfStopBits;
    private NumberOfDataBits numberOfDataBits;
    private Parity parity;
    //3rd octet extension, 5th octet
    private Boolean fullDuplex;//1 full duplex
    private ModemType modemType;

    private UserInformationLayer2Protocol userInformationLayer2Protocol;
    private UserInformationLayer3Protocol userInformationLayer3Protocol;
    private byte[] userInformationLayer3ProtocolExtension;

    public UserServiceInformation() {
    }

    public UserServiceInformation(CodingStandard codingStandard, InformationTransferCapability informationTransferCapability,
            TransferMode transferMode, InformationTransferRate informationTransferRate) {
        this.codingStandard = codingStandard;
        this.informationTransferCapability = informationTransferCapability;
        this.transferMode = transferMode;
        this.informationTransferRate = informationTransferRate;
    }

    @Override
    public byte[] encode() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int tmp = 1;//last octet indicator;
        tmp = (tmp << 2) | codingStandard.value();
        tmp = (tmp << 5) | informationTransferCapability.value();
        baos.write(tmp);

        tmp = 1;//last octet indicator
        tmp = (tmp << 2) | transferMode.value();
        tmp = (tmp << 5) | informationTransferRate.value();
        baos.write(tmp);

        if (this.checkNote1()) {
            if (rateMultiplier > 0) {
                baos.write(rateMultiplier);
            } else {
                throw new Exception("Rate multiplier octet is mandatory for MULTIRATE_64KBIT_BASE information transfer rate");
            }
        }

        if (userInformationLayer1Protocol != null) {
            //user information layer1 protocol extension
            if (isUserInformationLayer1ProtocolExtensionsPresent()) {
                tmp = 0;//not last octet of userInformationLayer1
                tmp = (tmp << 2) | LAYER1_IDENTITY;
                tmp = (tmp << 5) | this.userInformationLayer1Protocol.value();
                baos.write(tmp);
                baos.write(encodeUserInformationLayer1ProtocolExtensions());
            } else {
                tmp = 1;//last octet of userInformationLayer1
                tmp = (tmp << 2) | LAYER1_IDENTITY;
                tmp = (tmp << 5) | this.userInformationLayer1Protocol.value();
                baos.write(tmp);
            }
        }

        if (userInformationLayer2Protocol != null) {
            tmp = 1;//last octet of userInformationLayer2
            tmp = (tmp << 2) | LAYER2_IDENTITY;
            tmp = (tmp << 5) | userInformationLayer2Protocol.value();
            baos.write(tmp);
        }

        if (userInformationLayer3Protocol != null) {
            tmp = 0;//not last octet of userInformationLayer3
            tmp = (tmp << 2) | LAYER3_IDENTITY;
            tmp = (tmp << 5) | userInformationLayer3Protocol.value();
            baos.write(tmp);
            baos.write(userInformationLayer3ProtocolExtension);
        }
        return baos.toByteArray();
    }

    @Override
    public void decode(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        int tmp = bais.read();
        this.codingStandard = CodingStandard.getInstance((tmp >> 5) & 0b00000011);
        this.informationTransferCapability = InformationTransferCapability.getInstance(tmp & 0b00011111);

        tmp = bais.read();
        boolean lastOctet = ((tmp >> 7) & 0b00000001) == 1;
        this.transferMode = TransferMode.getInstance((tmp >> 5) & 0b00000011);
        this.informationTransferRate = InformationTransferRate.getInstance(tmp & 0b00011111);

        if (!lastOctet) {//rate multiplier coming
            if (informationTransferRate == InformationTransferRate.MULTIRATE_64KBIT_BASE) {
                this.rateMultiplier = bais.read();
            } else {
                throw new Exception("Rate multiplier required if Information Transfer Rate MULTIRATE 64kbit/s base rate selected");
            }
        }

        while (bais.available() > 0) {
            tmp = bais.read();
            lastOctet = ((tmp >> 7) & 0b00000001) == 1;
            int layerIdent = (tmp >> 5) & 0b00000011;
            switch (layerIdent) {
                case LAYER1_IDENTITY:
                    this.userInformationLayer1Protocol = UserInformationLayer1Protocol.getInstance(tmp & 0b00011111);
                    if (!lastOctet) {
                        this.decodeUserInformationLayer1Protocol(bais);
                    }
                    break;
                case LAYER2_IDENTITY:
                    userInformationLayer2Protocol = UserInformationLayer2Protocol.getInstance(tmp & 0b00011111);
                    break;
                case LAYER3_IDENTITY:
                    userInformationLayer3Protocol = UserInformationLayer3Protocol.getInstance(tmp & 0b00011111);
                    this.userInformationLayer3ProtocolExtension = new byte[2];
                    bais.read(userInformationLayer3ProtocolExtension);
                    break;
            }
        }
    }

    public CodingStandard getCodingStandard() {
        return codingStandard;
    }

    public void setCodingStandard(CodingStandard codingStandard) {
        this.codingStandard = codingStandard;
    }

    public InformationTransferCapability getInformationTransferCapability() {
        return informationTransferCapability;
    }

    public void setInformationTransferCapability(InformationTransferCapability informationTransferCapability) {
        this.informationTransferCapability = informationTransferCapability;
    }

    public InformationTransferRate getInformationTransferRate() {
        return informationTransferRate;
    }

    public void setInformationTransferRate(InformationTransferRate informationTransferRate) {
        this.informationTransferRate = informationTransferRate;
    }

    public int getRateMultiplier() {
        return rateMultiplier;
    }

    public void setRateMultiplier(int rateMultiplier) {
        this.rateMultiplier = rateMultiplier;
    }

    public TransferMode getTransferMode() {
        return transferMode;
    }

    public void setTransferMode(TransferMode transferMode) {
        this.transferMode = transferMode;
    }

    public UserInformationLayer1Protocol getUserInformationLayer1Protocol() {
        return userInformationLayer1Protocol;
    }

    public void setUserInformationLayer1Protocol(UserInformationLayer1Protocol userInformationLayer1Protocol) {
        this.userInformationLayer1Protocol = userInformationLayer1Protocol;
    }

    public Boolean getAsynchronousData() {
        return asynchronousData;
    }

    public void setAsynchronousData(Boolean asynchronousData) {
        this.asynchronousData = asynchronousData;
    }

    public Boolean getCanAcceptDataWithFlowControlMechanism() {
        return canAcceptDataWithFlowControlMechanism;
    }

    public void setCanAcceptDataWithFlowControlMechanism(Boolean canAcceptDataWithFlowControlMechanism) {
        this.canAcceptDataWithFlowControlMechanism = canAcceptDataWithFlowControlMechanism;
    }

    public Boolean getCanAcceptDataWithNetworkIndependentClock() {
        return canAcceptDataWithNetworkIndependentClock;
    }

    public void setCanAcceptDataWithNetworkIndependentClock(Boolean canAcceptDataWithNetworkIndependentClock) {
        this.canAcceptDataWithNetworkIndependentClock = canAcceptDataWithNetworkIndependentClock;
    }

    public Boolean getFullDuplex() {
        return fullDuplex;
    }

    public void setFullDuplex(Boolean fullDuplex) {
        this.fullDuplex = fullDuplex;
    }

    public Boolean getInBandNegotiationPossible() {
        return inBandNegotiationPossible;
    }

    public void setInBandNegotiationPossible(Boolean inBandNegotiationPossible) {
        this.inBandNegotiationPossible = inBandNegotiationPossible;
    }

    public IntermediateRate getIntermediateRate() {
        return intermediateRate;
    }

    public void setIntermediateRate(IntermediateRate intermediateRate) {
        this.intermediateRate = intermediateRate;
    }

    public LogicalLinkIdentifierNegotiation getLogicalLinkIdentifierNegotiation() {
        return logicalLinkIdentifierNegotiation;
    }

    public void setLogicalLinkIdentifierNegotiation(LogicalLinkIdentifierNegotiation logicalLinkIdentifierNegotiation) {
        this.logicalLinkIdentifierNegotiation = logicalLinkIdentifierNegotiation;
    }

    public Boolean getMessageOriginatorAssignorOnly() {
        return messageOriginatorAssignorOnly;
    }

    public void setMessageOriginatorAssignorOnly(Boolean messageOriginatorAssignorOnly) {
        this.messageOriginatorAssignorOnly = messageOriginatorAssignorOnly;
    }

    public ModemType getModemType() {
        return modemType;
    }

    public void setModemType(ModemType modemType) {
        this.modemType = modemType;
    }

    public ModeOfOperation getMode() {
        return mode;
    }

    public void setMode(ModeOfOperation mode) {
        this.mode = mode;
    }

    public Boolean getMultipleFrameEstablishmentSupported() {
        return multipleFrameEstablishmentSupported;
    }

    public void setMultipleFrameEstablishmentSupported(Boolean multipleFrameEstablishmentSupported) {
        this.multipleFrameEstablishmentSupported = multipleFrameEstablishmentSupported;
    }

    public Boolean getNegotiationIsDoneInBandUsingLogicalLinkZero() {
        return negotiationIsDoneInBandUsingLogicalLinkZero;
    }

    public void setNegotiationIsDoneInBandUsingLogicalLinkZero(Boolean negotiationIsDoneInBandUsingLogicalLinkZero) {
        this.negotiationIsDoneInBandUsingLogicalLinkZero = negotiationIsDoneInBandUsingLogicalLinkZero;
    }

    public NumberOfDataBits getNumberOfDataBits() {
        return numberOfDataBits;
    }

    public void setNumberOfDataBits(NumberOfDataBits numberOfDataBits) {
        this.numberOfDataBits = numberOfDataBits;
    }

    public NumberOfStopBits getNumberOfStopBits() {
        return numberOfStopBits;
    }

    public void setNumberOfStopBits(NumberOfStopBits numberOfStopBits) {
        this.numberOfStopBits = numberOfStopBits;
    }

    public Parity getParity() {
        return parity;
    }

    public void setParity(Parity parity) {
        this.parity = parity;
    }

    public Boolean getRateAdaptationHeaderIncluded() {
        return rateAdaptationHeaderIncluded;
    }

    public void setRateAdaptationHeaderIncluded(Boolean rateAdaptationHeaderIncluded) {
        this.rateAdaptationHeaderIncluded = rateAdaptationHeaderIncluded;
    }

    public Boolean getRequiredToSendDataWithFlowControlMechanism() {
        return requiredToSendDataWithFlowControlMechanism;
    }

    public void setRequiredToSendDataWithFlowControlMechanism(Boolean requiredToSendDataWithFlowControlMechanism) {
        this.requiredToSendDataWithFlowControlMechanism = requiredToSendDataWithFlowControlMechanism;
    }

    public Boolean getRequiredToSendDataWithNetworkIndependentClock() {
        return requiredToSendDataWithNetworkIndependentClock;
    }

    public void setRequiredToSendDataWithNetworkIndependentClock(Boolean requiredToSendDataWithNetworkIndependentClock) {
        this.requiredToSendDataWithNetworkIndependentClock = requiredToSendDataWithNetworkIndependentClock;
    }

    public UserRate getUserRate() {
        return userRate;
    }

    public void setUserRate(UserRate userRate) {
        this.userRate = userRate;
    }

    public UserInformationLayer2Protocol getUserInformationLayer2Protocol() {
        return userInformationLayer2Protocol;
    }

    public void setUserInformationLayer2Protocol(UserInformationLayer2Protocol userInformationLayer2Protocol) {
        this.userInformationLayer2Protocol = userInformationLayer2Protocol;
    }

    public UserInformationLayer3Protocol getUserInformationLayer3Protocol() {
        return userInformationLayer3Protocol;
    }

    public void setUserInformationLayer3Protocol(UserInformationLayer3Protocol userInformationLayer3Protocol) {
        this.userInformationLayer3Protocol = userInformationLayer3Protocol;
    }

    public void setUserInformationLayer3ProtocolExtension(byte[] userInformationLayer3ProtocolExtension) {
        this.userInformationLayer3ProtocolExtension = userInformationLayer3ProtocolExtension;
    }

    public byte[] getUserInformationLayer3ProtocolExtension() {
        return userInformationLayer3ProtocolExtension;
    }

//    //Encoding of ITU-T Q.931 specification bearer capability information element
    private byte[] encodeUserInformationLayer1ProtocolExtensions() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(5);

        //5a octet
        if (asynchronousData != null
                && inBandNegotiationPossible != null
                && userRate != null) {
            if (checkNote2()) {
                int b = 0;//extension bit
                b = (b << 1) | (asynchronousData ? 1 : 0);
                b = (b << 1) | (inBandNegotiationPossible ? 1 : 0);
                b = (b << 5) | userRate.value();
                baos.write(b);
            } else {
                throw new Exception("[Synch/asynch, Negot., UserRate] fields may be present if InformationTransferCapability indicates Unrestricted Digital information and "
                        + "UserInformationLayer1Protocol indicates either of the ITU-T V.110, I.460 and X.30 or V.120. It may also be present if "
                        + "InformationTransferCapability indicates 3.1kHz audio and UserInformationLayer1Protocol indicates any G.711");
            }

            //5b octet
            if (intermediateRate != null
                    && requiredToSendDataWithNetworkIndependentClock != null
                    && canAcceptDataWithNetworkIndependentClock != null
                    && requiredToSendDataWithFlowControlMechanism != null
                    && canAcceptDataWithFlowControlMechanism != null) {
                if (checkNote3()) {
                    int b = 0;//extension bit
                    b = (b << 2) | intermediateRate.value();
                    b = (b << 1) | (requiredToSendDataWithNetworkIndependentClock ? 1 : 0);
                    b = (b << 1) | (canAcceptDataWithNetworkIndependentClock ? 1 : 0);
                    b = (b << 1) | (requiredToSendDataWithFlowControlMechanism ? 1 : 0);
                    b = (b << 1) | (canAcceptDataWithFlowControlMechanism ? 1 : 0);
                    b = (b << 1);//spare
                    baos.write(b);
                } else {
                    throw new Exception("[IntermediateRate, NIC on Tx, NIC on Rx, Flow control on Tx, Flow control on Rx] "
                            + "only applies if UserInformationLayer1Protocol indicates ITU-T V.110,I.460 and X.30");
                }
            }

            //5b octet
            if (rateAdaptationHeaderIncluded != null
                    && multipleFrameEstablishmentSupported != null
                    && mode != null
                    && logicalLinkIdentifierNegotiation != null
                    && messageOriginatorAssignorOnly != null
                    && negotiationIsDoneInBandUsingLogicalLinkZero != null) {
                if (checkNote4()) {
                    int b = 0;//extension bit
                    b = (b << 1) | (rateAdaptationHeaderIncluded ? 1 : 0);
                    b = (b << 1) | (multipleFrameEstablishmentSupported ? 1 : 0);
                    b = (b << 1) | mode.value();
                    b = (b << 1) | logicalLinkIdentifierNegotiation.value();
                    b = (b << 1) | (messageOriginatorAssignorOnly ? 1 : 0);
                    b = (b << 1) | (negotiationIsDoneInBandUsingLogicalLinkZero ? 1 : 0);
                    b = b << 1;//spare
                    baos.write(b);
                } else {
                    throw new Exception("[Hdr/no Hdr, Multiframe, Mode, LLI negot., Assignor/ee, In-band neg.] "
                            + "only applies if UserInformationLayer1Protocol indicates ITU-T V.120");
                }
            }

            //5c
            if (numberOfStopBits != null
                    && numberOfDataBits != null
                    && parity != null) {
                if (checkNote2()) {
                    int b = 0;
                    b = (b << 2) | numberOfStopBits.value();
                    b = (b << 2) | numberOfDataBits.value();
                    b = (b << 3) | parity.value();
                    baos.write(b);
                } else {
                    throw new Exception("[NumberOfStopBits, NumberOfDataBits, Parity] fields may be present if InformationTransferCapability indicates Unrestricted Digital information and "
                            + "UserInformationLayer1Protocol indicates either of the ITU-T V.110, I.460 and X.30 or V.120. It may also be present if "
                            + "InformationTransferCapability indicates 3.1kHz audio and UserInformationLayer1Protocol indicates any G.711");
                }
            }

            //5d octet
            if (fullDuplex != null && modemType != null) {
                if (checkNote2()) {
                    int b = 0;//extension
                    b = (b << 1) | (fullDuplex ? 1 : 0);
                    b = (b << 6) | modemType.value();
                    baos.write(b);
                } else {
                    throw new Exception("[FullDuplex, ModemType] fields may be present if InformationTransferCapability indicates Unrestricted Digital information and "
                            + "UserInformationLayer1Protocol indicates either of the ITU-T V.110, I.460 and X.30 or V.120. It may also be present if "
                            + "InformationTransferCapability indicates 3.1kHz audio and UserInformationLayer1Protocol indicates any G.711");
                }
            }
        }

        byte[] data = baos.toByteArray();
        data[data.length - 1] = (byte) (data[data.length - 1] | 0b10000000);
        return data;
    }

    private boolean isUserInformationLayer1ProtocolExtensionsPresent() {
        return (asynchronousData != null && inBandNegotiationPossible != null && userRate != null)
                || (intermediateRate != null && requiredToSendDataWithNetworkIndependentClock != null && canAcceptDataWithNetworkIndependentClock != null && requiredToSendDataWithFlowControlMechanism != null && canAcceptDataWithFlowControlMechanism != null)
                || (rateAdaptationHeaderIncluded != null && multipleFrameEstablishmentSupported != null && mode != null && logicalLinkIdentifierNegotiation != null && messageOriginatorAssignorOnly != null && negotiationIsDoneInBandUsingLogicalLinkZero != null)
                || (numberOfStopBits != null && numberOfDataBits != null && parity != null)
                || (fullDuplex != null && modemType != null);
    }

    private void decodeUserInformationLayer1Protocol(ByteArrayInputStream bais) throws IOException, Exception {
        if (!checkNote2()) {
            throw new Exception("[Synch/asynch, Negot., UserRate] fields may be present if InformationTransferCapability indicates Unrestricted Digital information and "
                    + "UserInformationLayer1Protocol indicates either of the ITU-T V.110, I.460 and X.30 or V.120. It may also be present if "
                    + "InformationTransferCapability indicates 3.1kHz audio and UserInformationLayer1Protocol indicates any G.711");
        }

        //Processing 5a octet
        int b = bais.read();
        boolean lastOctet = ((b >> 7) & 0b00000001) == 1;
        this.asynchronousData = ((b >> 6) & 0b00000001) == 1;
        this.inBandNegotiationPossible = ((b >> 5) & 0b00000001) == 1;
        this.userRate = UserRate.getInstance(b & 0b00011111);

        //Processing 5b octet
        if (!lastOctet) {
            b = bais.read();
            lastOctet = ((b >> 7) & 0b00000001) == 1;
            //ITU-T Q.931 Bearer Capability Figure4 Note 3
            if (checkNote3()) {
                this.intermediateRate = IntermediateRate.getInstance((b >> 5) & 0b00000011);
                this.requiredToSendDataWithNetworkIndependentClock = ((b >> 4) & 0b00000001) == 1;
                this.canAcceptDataWithNetworkIndependentClock = ((b >> 3) & 0b00000001) == 1;
                this.requiredToSendDataWithFlowControlMechanism = ((b >> 2) & 0b00000001) == 1;
                this.canAcceptDataWithFlowControlMechanism = ((b >> 1) & 0b00000001) == 1;
            } else if (checkNote4()) {//ITU-T Q.931 Bearer Capability Figure4 Note 4
                this.rateAdaptationHeaderIncluded = ((b >> 6) & 0b00000001) == 1;
                this.multipleFrameEstablishmentSupported = ((b >> 5) & 0b00000001) == 1;
                this.mode = ModeOfOperation.getInstance((b >> 4) & 0b00000001);
                this.logicalLinkIdentifierNegotiation = LogicalLinkIdentifierNegotiation.getInstance((b >> 3) & 0b00000001);
                this.messageOriginatorAssignorOnly = ((b >> 2) & 0b00000001) == 1;
                this.negotiationIsDoneInBandUsingLogicalLinkZero = ((b >> 1) & 0b00000001) == 1;
            } else {
                throw new Exception("V.110, I.460, X.30  or V.120 ITU-T rate adaption required. Found: " + userInformationLayer1Protocol);
            }

            //5c octet
            if (!lastOctet) {
                if (!checkNote2()) {
                    throw new Exception("[NumberOfStopBits, NumberOfDataBits, Parity] fields may be present if InformationTransferCapability indicates Unrestricted Digital information and "
                            + "UserInformationLayer1Protocol indicates either of the ITU-T V.110, I.460 and X.30 or V.120. It may also be present if "
                            + "InformationTransferCapability indicates 3.1kHz audio and UserInformationLayer1Protocol indicates any G.711");
                }

                b = bais.read();
                lastOctet = ((b >> 7) & 0b00000001) == 1;

                this.numberOfStopBits = NumberOfStopBits.getInstance((b >> 5) & 0b00000011);
                this.numberOfDataBits = NumberOfDataBits.getInstance((b >> 3) & 0b00000011);
                this.parity = Parity.getInstance(b & 0b00000111);

                //5d octet
                if (!lastOctet) {
                    if (!checkNote2()) {
                        throw new Exception("[DuplexMode, ModemType] fields may be present if InformationTransferCapability indicates Unrestricted Digital information and "
                                + "UserInformationLayer1Protocol indicates either of the ITU-T V.110, I.460 and X.30 or V.120. It may also be present if "
                                + "InformationTransferCapability indicates 3.1kHz audio and UserInformationLayer1Protocol indicates any G.711");
                    }
                    b = bais.read();
                    this.fullDuplex = ((b >> 6) & 0b00000001) == 1;
                    this.modemType = ModemType.getInstance(b & 0b00111111);
                }
            }

        }
//        bais.mark(0);
//        boolean lastOctet = false;
//        int count = 0;
//        while (!lastOctet) {
//            int b = bais.read();
//            lastOctet = ((b >> 7) & 0b00000001) == 1;
//            count++;
//        }
//
//        bais.reset();
//        this.userInformationLayer1ProtocolExtension = new byte[count];
//        bais.read(userInformationLayer1ProtocolExtension);

    }

    private boolean checkNote1() {
        return informationTransferRate == InformationTransferRate.MULTIRATE_64KBIT_BASE;
    }

    private boolean checkNote2() {
        return (informationTransferCapability == InformationTransferCapability.UNRESTRICTED_DIGITAL_INFO
                && (userInformationLayer1Protocol == UserInformationLayer1Protocol.ITU_T_RATE_V110_I460_X30
                || userInformationLayer1Protocol == UserInformationLayer1Protocol.ITU_T_RATE_V_120))
                || (informationTransferCapability == InformationTransferCapability.AUDIO_3_1KHZ
                && (userInformationLayer1Protocol == UserInformationLayer1Protocol.G_711_A_LAW) || userInformationLayer1Protocol == UserInformationLayer1Protocol.G_711_MU_LAW);
    }

    private boolean checkNote3() {
        return userInformationLayer1Protocol == UserInformationLayer1Protocol.ITU_T_RATE_V110_I460_X30;
    }

    private boolean checkNote4() {
        return userInformationLayer1Protocol == UserInformationLayer1Protocol.ITU_T_RATE_V_120;
    }

    @Override
    public int getParameterCode() {
        return USER_SERVICE_INFORMATION;
    }

}
