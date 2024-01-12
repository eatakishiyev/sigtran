/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters.depricated;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;

/**
 *
 * @author eatakishiyev
 */
public class TP_PID {

    private PIDType pIDType;
    private TelematicInterworkingIndicator telematicInterworkingIndicator;
    private TelematicInterworking telematicInterworking;
    private ShortMessageType shortMessageType;

    public TP_PID(PIDType pIDType, TelematicInterworkingIndicator telematicInterworkingIndicator, TelematicInterworking telematicInterworking) {
        this.pIDType = pIDType;
        this.telematicInterworkingIndicator = telematicInterworkingIndicator;
        this.telematicInterworking = telematicInterworking;
    }

    public TP_PID(PIDType pIDType, ShortMessageType shortMessageType) {
        this.pIDType = pIDType;
        this.shortMessageType = shortMessageType;
    }

    public void decode(AsnInputStream ais) throws IOException {
        int decoded = ais.read();
        int y = decoded >> 6;
        switch (PIDType.getInstance(y)) {
            case pidType00:
                decode00(ais);
                break;
            case pidType01:
                decode01(ais);
                break;
        }
    }

    public void encode(AsnOutputStream aos) {
        switch (pIDType) {
            case pidType00:
                encode00(aos);
                break;
            case pidType01:
                encode01(aos);
                break;
        }
    }

    /**
     * @return the pIDType
     */
    public PIDType getpIDType() {
        return pIDType;
    }

    /**
     * @param pIDType the pIDType to set
     */
    public void setpIDType(PIDType pIDType) {
        this.pIDType = pIDType;
    }

    private void encode00(AsnOutputStream aos) {
        int encoded = 0;
        encoded = encoded | (pIDType.value << 6);
        encoded = encoded | (telematicInterworkingIndicator.value << 5);
        if (telematicInterworkingIndicator == TelematicInterworkingIndicator.telematicInterworking) {
            encoded = encoded | (telematicInterworking.value);
        }
        aos.write(encoded);
    }

    private void encode01(AsnOutputStream aos) {
        int encoded = 0;
        encoded = encoded | (pIDType.value << 6);
        encoded = encoded | (shortMessageType.value);
        aos.write(encoded);
    }

    private void decode00(AsnInputStream ais) throws IOException {
        int i = ais.read();
        int telematicIndicator = (i >> 5) & 1;

        telematicInterworkingIndicator = TelematicInterworkingIndicator.getInstance(telematicIndicator);
        switch (telematicInterworkingIndicator) {
            case telematicInterworking:
                int telematicInt = i & 0x1f;
                telematicInterworking = TelematicInterworking.getInstance(telematicInt);
                break;
        }
    }

    private void decode01(AsnInputStream ais) throws IOException {
        int i = ais.read();
        i = i & 0x3f;
        shortMessageType = ShortMessageType.getInstance(i);
    }

    public enum PIDType {

        pidType00(0),
        pidType01(1),
        pidType10(2),
        pidType11(3);
        private int value;

        private PIDType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static PIDType getInstance(int value) {
            switch (value) {
                case 0:
                    return pidType00;
                case 1:
                    return pidType01;
                case 2:
                    return pidType10;
                case 3:
                    return pidType11;
                default:
                    return null;
            }
        }
    }

    public enum TelematicInterworkingIndicator {

        noInterworking_SMEtoSMEProtocol(0),
        telematicInterworking(1);
        private int value;

        private TelematicInterworkingIndicator(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

        public static TelematicInterworkingIndicator getInstance(int value) {
            switch (value) {
                case 0:
                    return noInterworking_SMEtoSMEProtocol;
                case 1:
                    return telematicInterworking;
                default:
                    return null;
            }
        }
    }

    public enum ShortMessageType {

        Short_Message_Type_0(0),
        Replace_Short_Message_Type_1(1),
        Replace_Short_Message_Type_2(2),
        Replace_Short_Message_Type_3(3),
        Replace_Short_Message_Type_4(4),
        Replace_Short_Message_Type_5(5),
        Replace_Short_Message_Type_6(6),
        Replace_Short_Message_Type_7(7),
        Enhanced_Message_Service_Obsolete(30),
        Return_Call_Message(31),
        ANSI_136_R_DATA(60),
        ME_Data_download(61),
        ME_Depersonalization_Short_Message(62),
        U_SIM_Data_download(63);
        private int value;

        private ShortMessageType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static ShortMessageType getInstance(int value) {
            switch (value) {
                case 0:
                    return Short_Message_Type_0;
                case 1:
                    return Replace_Short_Message_Type_1;
                case 2:
                    return Replace_Short_Message_Type_2;
                case 3:
                    return Replace_Short_Message_Type_3;
                case 4:
                    return Replace_Short_Message_Type_4;
                case 5:
                    return Replace_Short_Message_Type_5;
                case 6:
                    return Replace_Short_Message_Type_6;
                case 7:
                    return Replace_Short_Message_Type_7;
                case 30:
                    return Enhanced_Message_Service_Obsolete;
                case 31:
                    return Return_Call_Message;
                case 60:
                    return ANSI_136_R_DATA;
                case 61:
                    return ME_Data_download;
                case 62:
                    return ME_Depersonalization_Short_Message;
                case 63:
                    return U_SIM_Data_download;
                default:
                    return null;
            }
        }
    }

    public enum TelematicInterworking {

        implicit(0),
        telex(1),
        group_3_telefax(2),
        group_4_telefax(3),
        voice_telephone(4),
        ERMES(5),
        National_Paging_system(6),
        Videotex(7),
        teletex_carrier_unspecified(8),
        teletex_in_PSPDN(9),
        teletex_in_CSPDN(10),
        teletex_in_analog_PSTN(11),
        teletex_in_digital_ISDN(12),
        UCI(13),
        reserved_14(14),
        reserved_15(15),
        a_message_handling_facility(16),
        any_public_X400_based_message_handling_system(17),
        Internet_Electronic_Mail(18),
        reserved_19(19),
        reserved_20(20),
        reserved_21(21),
        reserved_22(22),
        reserved_23(23),
        values_specific_to_each_SC_24(24),
        values_specific_to_each_SC_25(25),
        values_specific_to_each_SC_26(26),
        values_specific_to_each_SC_27(27),
        values_specific_to_each_SC_28(28),
        values_specific_to_each_SC_29(29),
        values_specific_to_each_SC_30(30);
        private int value;

        private TelematicInterworking(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static TelematicInterworking getInstance(int value) {
            switch (value) {
                case 0:
                    return implicit;
                case 1:
                    return telex;
                case 2:
                    return group_3_telefax;
                case 3:
                    return group_4_telefax;
                case 4:
                    return voice_telephone;
                case 5:
                    return ERMES;
                case 6:
                    return National_Paging_system;
                case 7:
                    return Videotex;
                case 8:
                    return teletex_carrier_unspecified;
                case 9:
                    return teletex_in_PSPDN;
                case 10:
                    return teletex_in_CSPDN;
                case 11:
                    return teletex_in_analog_PSTN;
                case 12:
                    return teletex_in_digital_ISDN;
                case 13:
                    return UCI;
                case 14:
                    return reserved_14;
                case 15:
                    return reserved_15;
                case 16:
                    return a_message_handling_facility;
                case 17:
                    return any_public_X400_based_message_handling_system;
                case 18:
                    return Internet_Electronic_Mail;
                case 19:
                    return reserved_19;
                case 20:
                    return reserved_20;
                case 21:
                    return reserved_21;
                case 22:
                    return reserved_22;
                case 23:
                    return reserved_23;
                case 24:
                    return values_specific_to_each_SC_24;
                case 25:
                    return values_specific_to_each_SC_25;
                case 26:
                    return values_specific_to_each_SC_26;
                case 27:
                    return values_specific_to_each_SC_27;
                case 28:
                    return values_specific_to_each_SC_28;
                case 29:
                    return values_specific_to_each_SC_29;
                case 30:
                    return values_specific_to_each_SC_30;
                default:
                    return null;

            }
        }
    }
}
