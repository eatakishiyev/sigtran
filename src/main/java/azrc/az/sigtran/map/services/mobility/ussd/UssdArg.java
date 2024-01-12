/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.mobility.ussd;

import azrc.az.sigtran.map.parameters.AlertingPattern;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.services.common.MAPArgument;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class UssdArg implements MAPArgument {

    private Integer ussdDataCodingScheme; //Mandatory parameter
    private byte[] ussdString;//Mandatory parameter
    private AlertingPattern alertingPatter;//Optional parameter
    private ISDNAddressString msisdn;//Optional parameter
    private byte[] requestData;
    protected boolean requestCorrupted = false;

    public UssdArg() {
    }

    public UssdArg(int ussdDataCodingScheme, byte[] ussdString) {
        this.ussdDataCodingScheme = ussdDataCodingScheme;
        this.ussdString = ussdString;
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException {
        this.requestData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSLA], "
                        + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException, java.io.IOException {
        // ussdDataCodingScheme
        int tag = ais.readTag();
        if (!ais.isTagPrimitive() || ais.getTagClass() != Tag.CLASS_UNIVERSAL || tag != Tag.STRING_OCTET) {
            throw new IncorrectSyntaxException(String.format("UssdDataCodingScheme: Expected Tag = 4 isPrimitive = true TagClass = 0, found Tag = %d isPrimitive = %b TagClass = %d",
                    tag, ais.isTagPrimitive(), ais.getTagClass()));
        }

        int length = ais.readLength();
        this.ussdDataCodingScheme = ais.read();

        // ussdString
        tag = ais.readTag();
        if (!ais.isTagPrimitive() || ais.getTagClass() != Tag.CLASS_UNIVERSAL || tag != Tag.STRING_OCTET) {
            throw new IncorrectSyntaxException(String.format("UssdString: Expected Tag = 4 isPrimitive = true TagClass = 0, found Tag = %d isPrimitive = %b TagClass = %d",
                    tag, ais.isTagPrimitive(), ais.getTagClass()));
        }
        length = ais.readLength();
        this.ussdString = new byte[length];
        ais.read(ussdString);

        while (ais.available() > 0) {
            tag = ais.readTag();
            switch (tag) {
                case Tag.STRING_OCTET://AlertingPattern
                    if (!ais.isTagPrimitive() || ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                        throw new IncorrectSyntaxException(String.format("AlertingPattern: Expected Tag = 4 isPrimitive = true TagClass = 0, found Tag = %d isPrimitive = %b TagClass = %d",
                                tag, ais.isTagPrimitive(), ais.getTagClass()));
                    }
                    length = ais.readLength();
                    this.alertingPatter = AlertingPattern.getInstance(ais.read());

                    break;
                case 0://msisdn
                    if (!ais.isTagPrimitive() || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                        throw new IncorrectSyntaxException(String.format("AlertingPattern: Expected Tag = 4 isPrimitive = true TagClass = 0, found Tag = %d isPrimitive = %b TagClass = %d",
                                tag, ais.isTagPrimitive(), ais.getTagClass()));
                    }
                    this.msisdn = new ISDNAddressString();
                    msisdn.decode(ais);
                    break;
                default:
                    throw new IncorrectSyntaxException(String.format("Unknown tag received: Tag = %d isPrimitive = %b TagClass = %d",
                            tag, ais.isTagPrimitive(), ais.getTagClass()));
            }
        }
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (this.ussdDataCodingScheme == null) {
                throw new IncorrectSyntaxException("Expecting mandatory parameter: UssdDataCodingScheme");
            }

            if (this.ussdString == null) {
                throw new IncorrectSyntaxException("Expecting mandatory parameter: UssdString");
            }

            //ussdDataCodingScheme
            aos.writeTag(Tag.CLASS_UNIVERSAL, true, Tag.STRING_OCTET);
            aos.writeLength(1);
            aos.write(this.ussdDataCodingScheme);

            //ussdString
            aos.writeOctetString(this.ussdString);

            if (this.alertingPatter != null) {
                aos.writeOctetString(new byte[]{(byte) alertingPatter.value()});
            }

            if (this.msisdn != null) {
                this.msisdn.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x00, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (Exception ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * The structure of the USSD-DataCodingScheme is defined by the Cell
     * Broadcast Data Coding Scheme as described in TS 3GPP TS 23.038 [25]
     *
     * @return the ussdDataCodingScheme
     */
    public int getUssdDataCodingScheme() {
        return ussdDataCodingScheme;
    }

    /**
     * The structure of the USSD-DataCodingScheme is defined by the Cell
     * Broadcast Data Coding Scheme as described in TS 3GPP TS 23.038 [25]
     *
     * @param ussdDataCodingScheme the ussdDataCodingScheme to set. This
     * parameter is mandatory
     */
    public void setUssdDataCodingScheme(int ussdDataCodingScheme) {
        this.ussdDataCodingScheme = ussdDataCodingScheme;
    }

    /**
     * @return the ussdString
     */
    public byte[] getUssdString() {
        return ussdString;
    }

    /**
     * @param ussdString the ussdString to set
     */
    public void setUssdString(byte[] ussdString) {
        this.ussdString = ussdString;
    }

    /**
     * @return the alertingPatter
     */
    public AlertingPattern getAlertingPatter() {
        return alertingPatter;
    }

    /**
     * @param alertingPatter the alertingPatter to set
     */
    public void setAlertingPatter(AlertingPattern alertingPatter) {
        this.alertingPatter = alertingPatter;
    }

    /**
     * @return the msisdn
     */
    public ISDNAddressString getMsisdn() {
        return msisdn;
    }

    /**
     * @param msisdn the msisdn to set
     */
    public void setMsisdn(ISDNAddressString msisdn) {
        this.msisdn = msisdn;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public boolean isRequestCorrupted() {
        return requestCorrupted;
    }

}
