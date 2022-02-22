/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.services.network.location.update;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.services.common.MAPArgument;
import dev.ocean.sigtran.map.parameters.ADDInfo;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.GSNAddress;
import dev.ocean.sigtran.map.parameters.IMSI;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import dev.ocean.sigtran.map.parameters.LMSI;
import dev.ocean.sigtran.map.parameters.VLRCapability;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class UpdateLocationArg implements MAPArgument {

    /**
     * UpdateLocationArg ::= SEQUENCE { imsi IMSI, msc-Number [1]
     * ISDN-AddressString, vlr-Number ISDN-AddressString, lmsi [10] LMSI
     * OPTIONAL, extensionContainer ExtensionContainer OPTIONAL, ... ,
     * vlr-Capability [6] VLR-Capability OPTIONAL, informPreviousNetworkEntity
     * [11] NULL OPTIONAL, cs-LCS-NotSupportedByUE [12] NULL OPTIONAL,
     * v-gmlc-Address [2] GSN-Address OPTIONAL, add-info [13] ADD-Info OPTIONAL,
     * pagingArea [14] PagingArea OPTIONAL, skipSubscriberDataUpdate [15] NULL
     * OPTIONAL, -- The skipSubscriberDataUpdate parameter in the
     * UpdateLocationArg and the ADD-Info -- structures carry the same semantic.
     * restorationIndicator [16] NULL OPTIONAL }
     */
    private IMSI imsi;
    private ISDNAddressString mscNumber;
    private ISDNAddressString vlrNumber;
    private LMSI lmsi;
    private ExtensionContainer extensionContainer;
    private VLRCapability vlrCapability;
    private Boolean informPreviousNetworkEntity = false;
    private Boolean csLCSNotSupportedByUE = false;
    private GSNAddress vGmlcAddress;
    private ADDInfo addInfo;
    private byte[] pagingArea;
    private Boolean skipSubscriberDataUpdate = false;
    private Boolean restorationIndicator = false;
    private byte[] requestData;
    protected boolean requestCorrupted = false;

    public UpdateLocationArg() {

    }

    public UpdateLocationArg(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.decode(data);
    }

    public UpdateLocationArg(IMSI imsi, ISDNAddressString mscNumber, ISDNAddressString vlrNumber) {
        this.imsi = imsi;
        this.mscNumber = mscNumber;
        this.vlrNumber = vlrNumber;
    }

    @Override
    public final void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            imsi.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);

            mscNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);

            vlrNumber.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);

            if (lmsi != null) {
                lmsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, lenPos, aos);
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_UNIVERSAL, Tag.STRING_OCTET, aos);
            }

            if (vlrCapability != null) {
                vlrCapability.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }

            if (informPreviousNetworkEntity) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 11);
            }

            if (csLCSNotSupportedByUE) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 12);
            }

            if (vGmlcAddress != null) {
                vGmlcAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }

            if (addInfo != null) {
                addInfo.encode(Tag.CLASS_CONTEXT_SPECIFIC, 13, aos);
            }

            if (pagingArea != null) {
                aos.writeOctetString(Tag.CLASS_CONTEXT_SPECIFIC, 14, pagingArea);
            }

            if (skipSubscriberDataUpdate) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 15);
            }

            if (restorationIndicator) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 16);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    @Override
    public final void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.requestData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this._decode(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    private void _decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            int tag = ais.readTag();

            if (tag == Tag.STRING_OCTET
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.imsi = new IMSI(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == 1
                    && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                this.mscNumber = new ISDNAddressString(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            tag = ais.readTag();
            if (tag == Tag.STRING_OCTET
                    && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                this.vlrNumber = new ISDNAddressString(ais);
            } else {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            while (ais.available() > 0) {
                tag = ais.readTag();
                switch (tag) {
                    case 10:
                        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                    + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                        }
                        this.lmsi = new LMSI(ais);
                        break;
                    case Tag.STRING_OCTET:
                        if (ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                    + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                        }
                        this.extensionContainer = new ExtensionContainer(ais);
                        break;
                    case 6:
                        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                    + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                        }
                        this.vlrCapability = new VLRCapability(ais);
                        break;
                    case 11:
                        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                    + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                        }
                        ais.readNull();
                        this.informPreviousNetworkEntity = true;
                        break;
                    case 12:
                        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                    + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                        }
                        ais.readNull();
                        this.csLCSNotSupportedByUE = true;
                        break;
                    case 2:
                        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                    + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                        }
                        this.vGmlcAddress = new GSNAddress(ais);
                        break;
                    case 13:
                        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                    + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                        }
                        this.addInfo = new ADDInfo(ais.readSequenceStream());
                        break;
                    case 14:
                        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                    + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                        }
                        this.pagingArea = ais.readOctetString();
                        break;
                    case 15:
                        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                    + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                        }
                        ais.readNull();
                        this.skipSubscriberDataUpdate = true;
                        break;
                    case 16:
                        if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                            throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                                    + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
                        }
                        ais.readNull();
                        this.restorationIndicator = true;
                        break;

                }
            }

        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    /**
     * @return the imsi
     */
    public IMSI getImsi() {
        return imsi;
    }

    /**
     * @param imsi the imsi to set
     */
    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    /**
     * @return the mscNumber
     */
    public ISDNAddressString getMscNumber() {
        return mscNumber;
    }

    /**
     * @param mscNumber the mscNumber to set
     */
    public void setMscNumber(ISDNAddressString mscNumber) {
        this.mscNumber = mscNumber;
    }

    /**
     * @return the vlrNumber
     */
    public ISDNAddressString getVlrNumber() {
        return vlrNumber;
    }

    /**
     * @param vlrNumber the vlrNumber to set
     */
    public void setVlrNumber(ISDNAddressString vlrNumber) {
        this.vlrNumber = vlrNumber;
    }

    /**
     * The Local Mobile Station Identity (LMSI) is a temporarily identification
     * that can be assigned to a mobile station that visits another network than
     * its home network. This supplementary LMSI may be assigned in the case
     * where the roaming telephone number (MSRN) is allocated on a call-by-call
     * basis. In these cases a LMSI can be used to speed up the search for the
     * subscriber data in the visitors location register (VLR).
     *
     * The LMSI is allocated by the VLR if the location is updated and is sent
     * to the home location register (HLR) together with the subscriber identity
     * (IMSI). However, the HLR does not make use of the LMSI. It includes it
     * together with the IMSI in all messages sent to the VLR concerning that
     * mobile station.
     *
     *
     * @return the lmsi
     */
    public LMSI getLmsi() {
        return lmsi;
    }

    /**
     *  * The Local Mobile Station Identity (LMSI) is a temporarily
     * identification that can be assigned to a mobile station that visits
     * another network than its home network. This supplementary LMSI may be
     * assigned in the case where the roaming telephone number (MSRN) is
     * allocated on a call-by-call basis. In these cases a LMSI can be used to
     * speed up the search for the subscriber data in the visitors location
     * register (VLR).
     *
     * The LMSI is allocated by the VLR if the location is updated and is sent
     * to the home location register (HLR) together with the subscriber identity
     * (IMSI). However, the HLR does not make use of the LMSI. It includes it
     * together with the IMSI in all messages sent to the VLR concerning that
     * mobile station.
     *
     * @param lmsi the lmsi to set
     */
    public void setLmsi(LMSI lmsi) {
        this.lmsi = lmsi;
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
     * @return the vlrCapability
     */
    public VLRCapability getVlrCapability() {
        return vlrCapability;
    }

    /**
     * @param vlrCapability the vlrCapability to set
     */
    public void setVlrCapability(VLRCapability vlrCapability) {
        this.vlrCapability = vlrCapability;
    }

    /**
     * @return the informPreviousNetworkEntity
     */
    public Boolean getInformPreviousNetworkEntity() {
        return informPreviousNetworkEntity;
    }

    /**
     * @param informPreviousNetworkEntity the informPreviousNetworkEntity to set
     */
    public void setInformPreviousNetworkEntity(Boolean informPreviousNetworkEntity) {
        this.informPreviousNetworkEntity = informPreviousNetworkEntity;
    }

    /**
     * @return the csLCSNotSupportedByUE
     */
    public Boolean getCsLCSNotSupportedByUE() {
        return csLCSNotSupportedByUE;
    }

    /**
     * @param csLCSNotSupportedByUE the csLCSNotSupportedByUE to set
     */
    public void setCsLCSNotSupportedByUE(Boolean csLCSNotSupportedByUE) {
        this.csLCSNotSupportedByUE = csLCSNotSupportedByUE;
    }

    /**
     * @return the vGmlcAddress
     */
    public GSNAddress getvGmlcAddress() {
        return vGmlcAddress;
    }

    /**
     * @param vGmlcAddress the vGmlcAddress to set
     */
    public void setvGmlcAddress(GSNAddress vGmlcAddress) {
        this.vGmlcAddress = vGmlcAddress;
    }

    /**
     * @return the addInfo
     */
    public ADDInfo getAddInfo() {
        return addInfo;
    }

    /**
     * @param addInfo the addInfo to set
     */
    public void setAddInfo(ADDInfo addInfo) {
        this.addInfo = addInfo;
    }

    /**
     * This parameter indicates, if present, the paging area where the MS is
     * currently located. Paging area is using for paging optimization purposes
     *
     * @return the pagingArea
     */
    public byte[] getPagingArea() {
        return pagingArea;
    }

    /**
     * * This parameter indicates, if present, the paging area where the MS is
     * currently located. Paging area is using for paging optimization purposes
     *
     * @param pagingArea the pagingArea to set
     */
    public void setPagingArea(byte[] pagingArea) {
        this.pagingArea = pagingArea;
    }

    /**
     * @return the skipSubscriberDataUpdate
     */
    public Boolean getSkipSubscriberDataUpdate() {
        return skipSubscriberDataUpdate;
    }

    /**
     * @param skipSubscriberDataUpdate the skipSubscriberDataUpdate to set
     */
    public void setSkipSubscriberDataUpdate(Boolean skipSubscriberDataUpdate) {
        this.skipSubscriberDataUpdate = skipSubscriberDataUpdate;
    }

    /**
     * @return the restorationIndicator
     */
    public Boolean getRestorationIndicator() {
        return restorationIndicator;
    }

    /**
     * @param restorationIndicator the restorationIndicator to set
     */
    public void setRestorationIndicator(Boolean restorationIndicator) {
        this.restorationIndicator = restorationIndicator;
    }

    public byte[] getRequestData() {
        return requestData;
    }

    public boolean isRequestCorrupted() {
        return requestCorrupted;
    }

}
