/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.location.cancellation;

import java.io.IOException;

import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.services.common.MAPArgument;
import azrc.az.sigtran.map.parameters.CancellationType;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.IMSI;
import azrc.az.sigtran.map.parameters.IMSIWithLMSI;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.map.parameters.LMSI;
import azrc.az.sigtran.map.parameters.TypeOfUpdate;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * CancelLocationArg ::= [3] SEQUENCE { identity Identity, cancellationType
 * CancellationType OPTIONAL, extensionContainer ExtensionContainer OPTIONAL,
 * ..., typeOfUpdate [0] TypeOfUpdate OPTIONAL, mtrf-SupportedAndAuthorized [1]
 * NULL OPTIONAL, mtrf-SupportedAndNotAuthorized [2] NULL OPTIONAL,
 * newMSC-Number [3] ISDN-AddressString OPTIONAL, newVLR-Number [4]
 * ISDN-AddressString OPTIONAL, new-lmsi [5] LMSI OPTIONAL }
 * --mtrf-SupportedAndAuthorized and mtrf-SupportedAndNotAuthorized shall not --
 * both be present
 *
 * @author eatakishiyev
 */
public class CancelLocationArg implements MAPArgument {

    private IMSI imsi;
    private IMSIWithLMSI imsiWithLMSI;
    private CancellationType cancellationType;
    private ExtensionContainer extensionContainer;
    private TypeOfUpdate typeOfUpdate;
    private boolean mtrfSupportedAndAuthorized = false;
    private boolean mtrfSupportedAndNotAuthorized = false;
    private ISDNAddressString newMSCNumber;
    private ISDNAddressString newVLRNumber;
    private LMSI newLMSI;

    private byte[] requestData;
    protected boolean requestCorrupted = false;

    public CancelLocationArg() {
    }

    public CancelLocationArg(IMSI imsi) {
        this.imsi = imsi;
    }

    public CancelLocationArg(IMSIWithLMSI imsiWithLMSI) {
        this.imsiWithLMSI = imsiWithLMSI;
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, false, 3);
            int lenPos = aos.StartContentDefiniteLength();
            //encoding identity
            if (imsi != null) {
                imsi.encode(aos);
            } else {
                imsiWithLMSI.encode(aos);
            }

            if (cancellationType != null) {
                aos.writeInteger(Tag.CLASS_UNIVERSAL, Tag.ENUMERATED, cancellationType.getValue());
            }

            if (extensionContainer != null) {
                extensionContainer.encode(aos);
            }

            if (typeOfUpdate != null) {
                aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, typeOfUpdate.getValue());
            }

            if (mtrfSupportedAndAuthorized) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 1);
            }

            if (mtrfSupportedAndNotAuthorized) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 2);
            }

            if (newMSCNumber != null) {
                newMSCNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }

            if (newVLRNumber != null) {
                newVLRNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }

            if (newLMSI != null) {
                newLMSI.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }

        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.requestData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != 3 || ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[3] TagClass[CONTEXT]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this._decode(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {

        }
    }

    private void _decode(AsnInputStream ais) throws IOException, UnexpectedDataException, IncorrectSyntaxException, AsnException {
        int tag = ais.readTag();
        //Identity decode
        if (tag == Tag.STRING_OCTET && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.imsi = new IMSI(ais);
        } else if (tag == Tag.SEQUENCE && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.imsiWithLMSI = new IMSIWithLMSI(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received", tag, ais.getTagClass()));
        }

        while (ais.available() > 0) {
            tag = ais.readTag();
            switch (tag) {
                case Tag.ENUMERATED:
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        cancellationType = CancellationType.getInstance((int) ais.readInteger());
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received", tag, ais.getTagClass()));
                    }
                    break;
                case Tag.SEQUENCE:
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        this.extensionContainer = new ExtensionContainer(ais);
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received", tag, ais.getTagClass()));
                    }
                    break;
                case 0:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        this.typeOfUpdate = TypeOfUpdate.getInstance((int) ais.readInteger());
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received", tag, ais.getTagClass()));
                    }
                    break;
                case 1:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        this.mtrfSupportedAndAuthorized = true;
                        ais.readNull();
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received", tag, ais.getTagClass()));
                    }
                    break;
                case 2:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        this.mtrfSupportedAndNotAuthorized = true;
                        ais.readNull();
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received", tag, ais.getTagClass()));
                    }
                    break;
                case 3:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        this.newMSCNumber = new ISDNAddressString(ais);
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received", tag, ais.getTagClass()));
                    }
                    break;
                case 4:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        this.newVLRNumber = new ISDNAddressString(ais);
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received", tag, ais.getTagClass()));
                    }
                    break;
                case 5:
                    if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                        this.newLMSI = new LMSI(ais);
                    } else {
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received", tag, ais.getTagClass()));
                    }
                    break;
                default:
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received", tag, ais.getTagClass()));
            }
        }
    }

    public IMSI getImsi() {
        return imsi;
    }

    public IMSIWithLMSI getImsiWithLMSI() {
        return imsiWithLMSI;
    }

    /**
     * @return the cancellationType
     */
    public CancellationType getCancellationType() {
        return cancellationType;
    }

    /**
     * @param cancellationType the cancellationType to set
     */
    public void setCancellationType(CancellationType cancellationType) {
        this.cancellationType = cancellationType;
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
     * @return the typeOfUpdate
     */
    public TypeOfUpdate getTypeOfUpdate() {
        return typeOfUpdate;
    }

    /**
     * @param typeOfUpdate the typeOfUpdate to set
     */
    public void setTypeOfUpdate(TypeOfUpdate typeOfUpdate) {
        this.typeOfUpdate = typeOfUpdate;
    }

    /**
     * @return the mtrfSupportedAndAuthorized
     */
    public boolean isMtrfSupportedAndAuthorized() {
        return mtrfSupportedAndAuthorized;
    }

    /**
     * @param mtrfSupportedAndAuthorized the mtrfSupportedAndAuthorized to set
     */
    public void setMtrfSupportedAndAuthorized(boolean mtrfSupportedAndAuthorized) {
        this.mtrfSupportedAndAuthorized = mtrfSupportedAndAuthorized;
    }

    /**
     * @return the mtrfSupportedAndNotAuthorized
     */
    public boolean isMtrfSupportedAndNotAuthorized() {
        return mtrfSupportedAndNotAuthorized;
    }

    /**
     * @param mtrfSupportedAndNotAuthorized the mtrfSupportedAndNotAuthorized to
     * set
     */
    public void setMtrfSupportedAndNotAuthorized(boolean mtrfSupportedAndNotAuthorized) {
        this.mtrfSupportedAndNotAuthorized = mtrfSupportedAndNotAuthorized;
    }

    /**
     * @return the newMSCNumber
     */
    public ISDNAddressString getNewMSCNumber() {
        return newMSCNumber;
    }

    /**
     * @param newMSCNumber the newMSCNumber to set
     */
    public void setNewMSCNumber(ISDNAddressString newMSCNumber) {
        this.newMSCNumber = newMSCNumber;
    }

    /**
     * @return the newVLRNumber
     */
    public ISDNAddressString getNewVLRNumber() {
        return newVLRNumber;
    }

    /**
     * @param newVLRNumber the newVLRNumber to set
     */
    public void setNewVLRNumber(ISDNAddressString newVLRNumber) {
        this.newVLRNumber = newVLRNumber;
    }

    /**
     * @return the newLMSI
     */
    public LMSI getNewLMSI() {
        return newLMSI;
    }

    /**
     * @param newLMSI the newLMSI to set
     */
    public void setNewLMSI(LMSI newLMSI) {
        this.newLMSI = newLMSI;
    }

    @Override
    public byte[] getRequestData() {
        return requestData;
    }

    @Override
    public boolean isRequestCorrupted() {
        return requestCorrupted;
    }

}
