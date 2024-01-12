/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class IP_SM_GW_Guidance  implements MAPParameter{

    private SMDeliveryTimerValue minimumDeliveryTimeValue;
    private SMDeliveryTimerValue recommendedDeliveryTimeValue;
    private ExtensionContainer extensionContainer;

    public IP_SM_GW_Guidance() {
    }

    public IP_SM_GW_Guidance(SMDeliveryTimerValue minimumDeliveryTimeValue, SMDeliveryTimerValue recommendedDeliveryTimeValue) {
        this.minimumDeliveryTimeValue = minimumDeliveryTimeValue;
        this.recommendedDeliveryTimeValue = recommendedDeliveryTimeValue;
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException {
        if (this.minimumDeliveryTimeValue == null || recommendedDeliveryTimeValue == null) {
            throw new IncorrectSyntaxException();
        }
        AsnOutputStream tmpAos = new AsnOutputStream();

        this.minimumDeliveryTimeValue.encode(Tag.CLASS_UNIVERSAL, Tag.INTEGER, tmpAos);
        this.recommendedDeliveryTimeValue.encode(Tag.CLASS_UNIVERSAL, Tag.INTEGER, tmpAos);

        if (this.extensionContainer != null) {
            this.extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, tmpAos);
        }
        try {
            aos.writeSequence(tagClass, tag, tmpAos.toByteArray());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int length = ais.readLength();
            byte[] data = new byte[length];
            ais.read(data);
            AsnInputStream tmpAis = new AsnInputStream(data);

            int tag = tmpAis.readTag();
            if (tmpAis.getTagClass() != Tag.CLASS_UNIVERSAL || !tmpAis.isTagPrimitive() || tag != Tag.INTEGER) {
                throw new IncorrectSyntaxException();
            }
            this.minimumDeliveryTimeValue = new SMDeliveryTimerValue();
            this.minimumDeliveryTimeValue.decode(tmpAis);

            tag = tmpAis.readTag();
            if (tmpAis.getTagClass() != Tag.CLASS_UNIVERSAL || !tmpAis.isTagPrimitive() || tag != Tag.INTEGER) {
                throw new IncorrectSyntaxException();
            }

            this.recommendedDeliveryTimeValue = new SMDeliveryTimerValue();
            this.recommendedDeliveryTimeValue.decode(tmpAis);

            if (tmpAis.available() > 0) {
                tag = tmpAis.readTag();
                if (tmpAis.getTagClass() != Tag.CLASS_UNIVERSAL || tmpAis.isTagPrimitive() || tag != Tag.SEQUENCE) {
                    throw new IncorrectSyntaxException();
                }

                this.extensionContainer = new ExtensionContainer();
                this.extensionContainer.decode(tmpAis);
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the minimumDeliveryTimeValue
     */
    public SMDeliveryTimerValue getMinimumDeliveryTimeValue() {
        return minimumDeliveryTimeValue;
    }

    /**
     * @param minimumDeliveryTimeValue the minimumDeliveryTimeValue to set
     */
    public void setMinimumDeliveryTimeValue(SMDeliveryTimerValue minimumDeliveryTimeValue) {
        this.minimumDeliveryTimeValue = minimumDeliveryTimeValue;
    }

    /**
     * @return the recommendedDeliveryTimeValue
     */
    public SMDeliveryTimerValue getRecommendedDeliveryTimeValue() {
        return recommendedDeliveryTimeValue;
    }

    /**
     * @param recommendedDeliveryTimeValue the recommendedDeliveryTimeValue to
     * set
     */
    public void setRecommendedDeliveryTimeValue(SMDeliveryTimerValue recommendedDeliveryTimeValue) {
        this.recommendedDeliveryTimeValue = recommendedDeliveryTimeValue;
    }

    /**
     * @return the extensionContainer
     */
    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    /**
     * @param extensionContainer the extensionContainer to set
     */
    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    @Override
    public String toString() {
        return "IP_SM_GW_Guidance{" +
                "minimumDeliveryTimeValue=" + minimumDeliveryTimeValue +
                ", recommendedDeliveryTimeValue=" + recommendedDeliveryTimeValue +
                ", extensionContainer=" + extensionContainer +
                '}';
    }
}
