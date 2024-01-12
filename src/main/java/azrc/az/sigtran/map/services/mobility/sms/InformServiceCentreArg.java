/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.mobility.sms;

import java.io.IOException;

import azrc.az.sigtran.map.parameters.AbsentSubscriberDiagnosticSM;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.map.parameters.MWStatus;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import azrc.az.sigtran.map.services.common.MAPArgument;

/**
 *
 * @author eatakishiyev
 */
public class InformServiceCentreArg implements MAPArgument {

    private ISDNAddressString storedMsisdn;
    private MWStatus mwStatus;
    private ExtensionContainer extensionContainer;
    private AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM;
    private AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM;
    private byte[] requestData;
    protected boolean requestCorrupted = false;

    @Override
    public String toString() {
        return new StringBuilder().append("InformServiceCentre:[")
                .append("StoredMSISDN = ").append(storedMsisdn)
                .append(";MWStatus = ").append(mwStatus)
                .append(";AbsentSubscriberDiagnosticSM = ").append(absentSubscriberDiagnosticSM)
                .append(";AdditionalAbsentSubscriberDiagnosticSM = ").append(additionalAbsentSubscriberDiagnosticSM)
                .append("]").toString();
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (storedMsisdn != null) {
                this.storedMsisdn.encode(Tag.CLASS_CONTEXT_SPECIFIC, Tag.STRING_OCTET, aos);
            }

            if (mwStatus != null) {
                this.mwStatus.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_BIT, aos);
            }

            if (this.extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
            }

            if (this.absentSubscriberDiagnosticSM != null) {
                this.absentSubscriberDiagnosticSM.encode(Tag.CLASS_UNIVERSAL, Tag.INTEGER, aos);
            }
            if (this.additionalAbsentSubscriberDiagnosticSM != null) {
                this.additionalAbsentSubscriberDiagnosticSM.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0x00, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.requestData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSLA], "
                        + "found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this.decode_(ais.readSequenceStream());
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void decode_(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive() && tag == Tag.STRING_OCTET) {
                    this.storedMsisdn = new ISDNAddressString();
                    this.storedMsisdn.decode(ais);
                } else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive() && tag == Tag.STRING_BIT) {
                    this.mwStatus = new MWStatus();
                    this.mwStatus.decode(ais);
                } else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && !ais.isTagPrimitive() && tag == Tag.SEQUENCE) {
                    this.extensionContainer = new ExtensionContainer();
                    this.extensionContainer.decode(ais);
                } else if (ais.getTagClass() == Tag.CLASS_UNIVERSAL && ais.isTagPrimitive() && tag == Tag.INTEGER) {
                    this.absentSubscriberDiagnosticSM = new AbsentSubscriberDiagnosticSM();
                    this.absentSubscriberDiagnosticSM.decode(ais);
                } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC && ais.isTagPrimitive() && tag == 0x00) {
                    this.additionalAbsentSubscriberDiagnosticSM = new AbsentSubscriberDiagnosticSM();
                    this.additionalAbsentSubscriberDiagnosticSM.decode(ais);
                } else {
                    throw new IncorrectSyntaxException("Unknown parameter");
                }
            }
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the storedMsisdn
     */
    public ISDNAddressString getStoredMsisdn() {
        return storedMsisdn;
    }

    /**
     * @param storedMsisdn the storedMsisdn to set
     */
    public void setStoredMsisdn(ISDNAddressString storedMsisdn) {
        this.storedMsisdn = storedMsisdn;
    }

    /**
     * @return the mwStatus
     */
    public MWStatus getMwStatus() {
        return mwStatus;
    }

    /**
     * @param mwStatus the mwStatus to set
     */
    public void setMwStatus(MWStatus mwStatus) {
        this.mwStatus = mwStatus;
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

    /**
     * @return the absentSubscriberDiagnosticSM
     */
    public AbsentSubscriberDiagnosticSM getAbsentSubscriberDiagnosticSM() {
        return absentSubscriberDiagnosticSM;
    }

    /**
     * @param absentSubscriberDiagnosticSM the absentSubscriberDiagnosticSM to
     * set
     */
    public void setAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSM absentSubscriberDiagnosticSM) {
        this.absentSubscriberDiagnosticSM = absentSubscriberDiagnosticSM;
    }

    /**
     * @return the additionalAbsentSubscriberDiagnosticSM
     */
    public AbsentSubscriberDiagnosticSM getAdditionalAbsentSubscriberDiagnosticSM() {
        return additionalAbsentSubscriberDiagnosticSM;
    }

    /**
     * @param additionalAbsentSubscriberDiagnosticSM the
     * additionalAbsentSubscriberDiagnosticSM to set
     */
    public void setAdditionalAbsentSubscriberDiagnosticSM(AbsentSubscriberDiagnosticSM additionalAbsentSubscriberDiagnosticSM) {
        this.additionalAbsentSubscriberDiagnosticSM = additionalAbsentSubscriberDiagnosticSM;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public boolean isRequestCorrupted() {
        return requestCorrupted;
    }
}
