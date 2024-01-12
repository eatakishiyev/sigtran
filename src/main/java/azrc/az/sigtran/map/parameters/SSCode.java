/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class SSCode {

//    SS-Code  ::= OCTET STRING (SIZE (1)) 
//  -- This type is used to represent the code identifying a single 
//  -- supplementary service, a group of supplementary services, or 
//  -- all supplementary services. The services and abbreviations 
//  -- used are defined in TS 3GPP TS 22.004 [5]. The internal structure is 
//  -- defined as follows: 
// -- 
//  -- bits 87654321: group (bits 8765), and specific service 
//  -- (bits 4321)
    private SSCodes ssCode;

    public SSCode() {
    }

    public SSCode(SSCodes ssCode) {
        this.ssCode = ssCode;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeOctetString(tagClass, tag, new byte[]{(byte) ssCode.value});
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            int length = ais.readLength();
            if (length > 1) {
                throw new IncorrectSyntaxException("Unexpected parameter length. Expecting length is 1. Received " + length);
            }

            byte value = ais.readOctetStringData(length)[0];
            this.ssCode = SSCodes.getInstance(value);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the ssCode
     */
    public SSCodes getSsCode() {
        return ssCode;
    }

    /**
     * @param ssCode the ssCode to set
     */
    public void setSsCode(SSCodes ssCode) {
        this.ssCode = ssCode;
    }

    public enum SSCodes {

        ALL_SS(0),
        ALL_LINE_IDENTIFICATION_SS(16),
        CLIP(17),
        CLIR(18),
        COLP(19),
        COLR(20),
        MCI(21),
        ALL_NAME_IDENTIFICATION_SS(24),
        CNAP(25),
        ALL_FORWARDING_SS(32),
        CFU(33),
        ALL_COND_FORWARDING_SS(40),
        CFB(41),
        CFNRY(42),
        CFNRC(43),
        CD(36),
        ALL_CALL_OFFERING_SS(48),
        ECT(49),
        MAH(50),
        ALL_CALL_COMPLETION_SS(64),
        CW(65),
        HOLD(66),
        CCBSA(67),
        CCBSB(68),
        MC(69),
        ALL_MULTI_PARTY_SS(80),
        MULTIPTY(81),
        ALL_COMMUNITY_OF_INTEREST_SS(96),
        CUG(97),
        ALL_CHARGING_SS(112),
        AOCI(113),
        AOCC(114),
        ALL_ADDITIONAL_INFO_TRANSFER_SS(128),
        UUS1(129),
        UUS2(130),
        UUS3(131),
        ALL_BARING_SS(144),
        BARRING_OF_OUTGOING_CALLS(145),
        BAOC(146),
        BOIC(147),
        BOIC_EX_HC(148),
        BARRING_OF_INCOMING_CALLS(153),
        BAIC(154),
        BIC_ROAM(155),
        ALL_PLMN_SPECIFIC_SS(240),
        PLMN_SPECIFIC_SS1(241),
        PLMN_SPECIFIC_SS2(242),
        PLMN_SPECIFIC_SS3(243),
        PLMN_SPECIFIC_SS4(244),
        PLMN_SPECIFIC_SS5(245),
        PLMN_SPECIFIC_SS6(246),
        PLMN_SPECIFIC_SS7(247),
        PLMN_SPECIFIC_SS8(248),
        PLMN_SPECIFIC_SS9(249),
        PLMN_SPECIFIC_SSA(250),
        PLMN_SPECIFIC_SSB(251),
        PLMN_SPECIFIC_SSC(252),
        PLMN_SPECIFIC_SSD(253),
        PLMN_SPECIFIC_SSE(254),
        PLMN_SPECIFIC_SSF(255),
        ALL_CALL_PRIORITY_SS(160),
        EMLPP(161),
        ALL_LCS_PRIVACY_EXCEPTION(176),
        UNIVERSAL(177),
        CALL_SESSION_RELATED(178),
        CALL_SESSION_UNRELATED(179),
        PLMN_OPERATOR(180),
        SERVICE_TYPE(181),
        ALL_MOLRSS(192),
        BASIC_SELF_LOCATION(193),
        AUTONOMOUS_SELF_LOCATION(194),
        TRANSFER_TO_THIRD_PARTY(195);

        private final int value;

        private SSCodes(int value) {
            this.value = value;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        public static SSCodes getInstance(int value) {
            switch (value) {
                case 0:
                    return ALL_SS;
                case 16:
                    return ALL_LINE_IDENTIFICATION_SS;
                case 17:
                    return CLIP;
                case 18:
                    return CLIR;
                case 19:
                    return COLP;
                case 20:
                    return COLR;
                case 21:
                    return MCI;
                case 24:
                    return ALL_NAME_IDENTIFICATION_SS;
                case 25:
                    return CNAP;
                case 32:
                    return ALL_FORWARDING_SS;
                case 33:
                    return CFU;
                case 40:
                    return ALL_COND_FORWARDING_SS;
                case 41:
                    return CFB;
                case 42:
                    return CFNRY;
                case 43:
                    return CFNRC;
                case 36:
                    return CD;
                case 48:
                    return ALL_CALL_OFFERING_SS;
                case 49:
                    return ECT;
                case 50:
                    return MAH;
                case 64:
                    return ALL_CALL_COMPLETION_SS;
                case 65:
                    return CW;
                case 66:
                    return HOLD;
                case 67:
                    return CCBSA;
                case 68:
                    return CCBSB;
                case 69:
                    return MC;
                case 80:
                    return ALL_MULTI_PARTY_SS;
                case 81:
                    return MULTIPTY;
                case 96:
                    return ALL_COMMUNITY_OF_INTEREST_SS;
                case 97:
                    return CUG;
                case 112:
                    return ALL_CHARGING_SS;
                case 113:
                    return AOCI;
                case 114:
                    return AOCC;
                case 128:
                    return ALL_ADDITIONAL_INFO_TRANSFER_SS;
                case 129:
                    return UUS1;
                case 130:
                    return UUS2;
                case 131:
                    return UUS3;
                case 144:
                    return ALL_BARING_SS;
                case 145:
                    return BARRING_OF_OUTGOING_CALLS;
                case 146:
                    return BAOC;
                case 147:
                    return BOIC;
                case 148:
                    return BOIC_EX_HC;
                case 153:
                    return BARRING_OF_INCOMING_CALLS;
                case 154:
                    return BAIC;
                case 155:
                    return BIC_ROAM;
                case 240:
                    return ALL_PLMN_SPECIFIC_SS;
                case 241:
                    return PLMN_SPECIFIC_SS1;
                case 242:
                    return PLMN_SPECIFIC_SS2;
                case 243:
                    return PLMN_SPECIFIC_SS3;
                case 244:
                    return PLMN_SPECIFIC_SS4;
                case 245:
                    return PLMN_SPECIFIC_SS5;
                case 246:
                    return PLMN_SPECIFIC_SS6;
                case 247:
                    return PLMN_SPECIFIC_SS7;
                case 248:
                    return PLMN_SPECIFIC_SS8;
                case 249:
                    return PLMN_SPECIFIC_SS9;
                case 250:
                    return PLMN_SPECIFIC_SSA;
                case 251:
                    return PLMN_SPECIFIC_SSB;
                case 252:
                    return PLMN_SPECIFIC_SSC;
                case 253:
                    return PLMN_SPECIFIC_SSD;
                case 254:
                    return PLMN_SPECIFIC_SSE;
                case 255:
                    return PLMN_SPECIFIC_SSF;
                case 160:
                    return ALL_CALL_PRIORITY_SS;
                case 161:
                    return EMLPP;
                case 176:
                    return ALL_LCS_PRIVACY_EXCEPTION;
                case 177:
                    return UNIVERSAL;
                case 178:
                    return CALL_SESSION_RELATED;
                case 179:
                    return CALL_SESSION_UNRELATED;
                case 180:
                    return PLMN_OPERATOR;
                case 181:
                    return SERVICE_TYPE;
                case 192:
                    return ALL_MOLRSS;
                case 193:
                    return BASIC_SELF_LOCATION;
                case 194:
                    return AUTONOMOUS_SELF_LOCATION;
                case 195:
                    return TRANSFER_TO_THIRD_PARTY;
                default:
                    return null;
            }
        }
    }

}
