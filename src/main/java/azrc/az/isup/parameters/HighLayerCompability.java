/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.isup.parameters;

import azrc.az.isup.enums.ExtendedAudovisiulCharacteristicsIdentification;
import azrc.az.isup.enums.HighLayerCharacteristicsIdentification;
import azrc.az.isup.enums.PresentationMethod;
import azrc.az.isup.enums.Interpretation;
import azrc.az.isup.enums.CodingStandard;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author eatakishiyev
 */
public class HighLayerCompability {

    //ITU Q.931 Specification 4.5.17 subclause
    private CodingStandard codingStandard = null;
    private Interpretation interpretation = null;
    private PresentationMethod presentationMethod = null;
    private HighLayerCharacteristicsIdentification highLayerCharacteristicsIdentification = null;
    private HighLayerCharacteristicsIdentification extendedHighLayerCharacteristicsIdentification = null;
    private ExtendedAudovisiulCharacteristicsIdentification extendedAudovisiulCharacteristicsIdentification = null;

    private byte[] highLayerCompability;

    public HighLayerCompability() {
    }


    public HighLayerCompability(CodingStandard codingStandard, Interpretation interpretation, PresentationMethod presentationMethod, HighLayerCharacteristicsIdentification highLayerCharacteristicsIdentification) {
        this.codingStandard = codingStandard;
        this.interpretation = interpretation;
        this.presentationMethod = presentationMethod;
        this.highLayerCharacteristicsIdentification = highLayerCharacteristicsIdentification;
    }

    public void encode(ByteArrayOutputStream baos) throws IOException {
        if (highLayerCompability != null) {
            baos.write(highLayerCompability);
        } else {
            int firstOctet = 1;
            firstOctet = firstOctet << 2;
            firstOctet = firstOctet | codingStandard.value();
            firstOctet = firstOctet << 3;
            firstOctet = firstOctet | interpretation.getValue();
            firstOctet = firstOctet << 2;
            firstOctet = firstOctet | presentationMethod.getValue();
            baos.write(firstOctet);

            int secondOctet = (extendedHighLayerCharacteristicsIdentification != null ? 0 : 1);
            secondOctet = secondOctet << 7;
            secondOctet = secondOctet | highLayerCharacteristicsIdentification.getValue();

            baos.write(secondOctet);

            if (extendedHighLayerCharacteristicsIdentification != null) {
                if (highLayerCharacteristicsIdentification == HighLayerCharacteristicsIdentification.RESERVED_FOR_MAINTENANCE
                        || highLayerCharacteristicsIdentification == HighLayerCharacteristicsIdentification.RESERVED_FOR_MANAGEMENT) {
                    int thirthOctet = 1 << 7;
                    thirthOctet = thirthOctet | extendedHighLayerCharacteristicsIdentification.getValue();
                    baos.write(thirthOctet);
                } else {
                    throw new IOException("Expecting Maintenance or Management High layer characteristics identification");
                }
            }

            if (extendedAudovisiulCharacteristicsIdentification != null) {
                if (highLayerCharacteristicsIdentification == HighLayerCharacteristicsIdentification.AUDIO_GRAPHIC_CONFERENCE) {
                    int fourthOctet = 1 << 7;
                    fourthOctet = fourthOctet | extendedAudovisiulCharacteristicsIdentification.getValue();
                    baos.write(fourthOctet);
                } else {
                    throw new IOException("Expecting Audio Visual High layer characteristics identification");
                }
            }
        }
    }

    public byte[] encode() throws IOException {
        if (highLayerCompability != null) {
            return this.highLayerCompability;
        } else {
            byte[] data;
            if (extendedAudovisiulCharacteristicsIdentification != null | extendedHighLayerCharacteristicsIdentification != null) {
                data = new byte[3];
            } else {
                data = new byte[2];
            }

            int firstOctet = 1;
            firstOctet = firstOctet << 2;
            firstOctet = firstOctet | codingStandard.value();
            firstOctet = firstOctet << 3;
            firstOctet = firstOctet | interpretation.getValue();
            firstOctet = firstOctet << 2;
            firstOctet = firstOctet | presentationMethod.getValue();
            data[0] = (byte) firstOctet;

            int secondOctet = (extendedHighLayerCharacteristicsIdentification != null ? 0 : 1);
            secondOctet = secondOctet << 7;
            secondOctet = secondOctet | highLayerCharacteristicsIdentification.getValue();

            data[1] = (byte) secondOctet;

            if (extendedHighLayerCharacteristicsIdentification != null) {
                if (highLayerCharacteristicsIdentification == HighLayerCharacteristicsIdentification.RESERVED_FOR_MAINTENANCE
                        || highLayerCharacteristicsIdentification == HighLayerCharacteristicsIdentification.RESERVED_FOR_MANAGEMENT) {
                    int thirthOctet = 1 << 7;
                    thirthOctet = thirthOctet | extendedHighLayerCharacteristicsIdentification.getValue();
                    data[2] = (byte) thirthOctet;
                } else {
                    throw new IOException("Expecting Maintenance or Management High layer characteristics identification");
                }
            }

            if (extendedAudovisiulCharacteristicsIdentification != null) {
                if (highLayerCharacteristicsIdentification == HighLayerCharacteristicsIdentification.AUDIO_GRAPHIC_CONFERENCE) {
                    int thirthOctet = 1 << 7;
                    thirthOctet = thirthOctet | extendedAudovisiulCharacteristicsIdentification.getValue();
                    data[2] = (byte) thirthOctet;
                } else {
                    throw new IOException("Expecting Audio Visual High layer characteristics identification");
                }
            }
            return data;
        }
    }

    public void decode(ByteArrayInputStream bais) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int firstOctet = bais.read();
        baos.write(firstOctet);

        this.codingStandard = CodingStandard.getInstance(((firstOctet >> 5) & 0x03));
        this.interpretation = Interpretation.getInstance(((firstOctet >> 2) & 0x07));
        this.presentationMethod = PresentationMethod.getInstance(((firstOctet) & 0x03));
        int secondOctet = bais.read();
        baos.write(secondOctet);

        byte ext = (byte) ((secondOctet >> 7) & 0x01);
        if (ext == 1) {
            this.highLayerCharacteristicsIdentification = HighLayerCharacteristicsIdentification.getInstance(secondOctet & 0x7F);
            switch (highLayerCharacteristicsIdentification) {
                case RESERVED_FOR_MAINTENANCE:
                case RESERVED_FOR_MANAGEMENT:
                    int octet = bais.read();
                    baos.write(octet);
                    this.extendedHighLayerCharacteristicsIdentification = HighLayerCharacteristicsIdentification.getInstance(octet & 0x7F);
                    break;
                case RESERVED_AUDIO_VISUAL1:
                case RESERVED_AUDIO_VISUAL2:
                case RESERVED_AUDIO_VISUAL3:
                case RESERVED_AUDIO_VISUAL4:
                case RESERVED_AUDIO_VISUAL5:
                    octet = bais.read();
                    baos.write(octet);
                    this.extendedAudovisiulCharacteristicsIdentification = ExtendedAudovisiulCharacteristicsIdentification.getInstance(octet & 0x7F);
                    break;
            }
        }
        this.highLayerCompability = baos.toByteArray();
    }

    /**
     * @return the codingStandard
     */
    public CodingStandard getCodingStandard() {
        return codingStandard;
    }

    /**
     * @param codingStandard the codingStandard to set
     */
    public void setCodingStandard(CodingStandard codingStandard) {
        this.codingStandard = codingStandard;
    }

    /**
     * @return the interpretation
     */
    public Interpretation getInterpretation() {
        return interpretation;
    }

    /**
     * @param interpretation the interpretation to set
     */
    public void setInterpretation(Interpretation interpretation) {
        this.interpretation = interpretation;
    }

    /**
     * @return the presentationMethod
     */
    public PresentationMethod getPresentationMethod() {
        return presentationMethod;
    }

    /**
     * @param presentationMethod the presentationMethod to set
     */
    public void setPresentationMethod(PresentationMethod presentationMethod) {
        this.presentationMethod = presentationMethod;
    }

    /**
     * @return the highLayerCharacteristicsIdentification
     */
    public HighLayerCharacteristicsIdentification getHighLayerCharacteristicsIdentification() {
        return highLayerCharacteristicsIdentification;
    }

    /**
     * @param highLayerCharacteristicsIdentification the
     * highLayerCharacteristicsIdentification to set
     */
    public void setHighLayerCharacteristicsIdentification(HighLayerCharacteristicsIdentification highLayerCharacteristicsIdentification) {
        this.highLayerCharacteristicsIdentification = highLayerCharacteristicsIdentification;
    }

    /**
     * @return the extendedHighLayerCharacteristicsIdentification
     */
    public HighLayerCharacteristicsIdentification getExtendedHighLayerCharacteristicsIdentification() {
        return extendedHighLayerCharacteristicsIdentification;
    }

    /**
     * @param extendedHighLayerCharacteristicsIdentification the
     * extendedHighLayerCharacteristicsIdentification to set
     */
    public void setExtendedHighLayerCharacteristicsIdentification(HighLayerCharacteristicsIdentification extendedHighLayerCharacteristicsIdentification) {
        this.extendedHighLayerCharacteristicsIdentification = extendedHighLayerCharacteristicsIdentification;
    }

    /**
     * @return the extendedAudovisiulCharacteristicsIdentification
     */
    public ExtendedAudovisiulCharacteristicsIdentification getExtendedAudovisiulCharacteristicsIdentification() {
        return extendedAudovisiulCharacteristicsIdentification;
    }

    /**
     * @param extendedAudovisiulCharacteristicsIdentification the
     * extendedAudovisiulCharacteristicsIdentification to set
     */
    public void setExtendedAudovisiulCharacteristicsIdentification(ExtendedAudovisiulCharacteristicsIdentification extendedAudovisiulCharacteristicsIdentification) {
        this.extendedAudovisiulCharacteristicsIdentification = extendedAudovisiulCharacteristicsIdentification;
    }

    public byte[] getHighLayerCompability() {
        return highLayerCompability;
    }
}
