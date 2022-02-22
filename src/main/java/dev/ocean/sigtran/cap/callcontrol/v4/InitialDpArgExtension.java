/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap.callcontrol.v4;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import dev.ocean.isup.parameters.CalledPartyNumber;
import dev.ocean.isup.parameters.HighLayerCompability;
import dev.ocean.sigtran.common.exceptions.IllegalNumberFormatException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.parameters.ExtBasicServiceCode;
import dev.ocean.sigtran.map.parameters.IMEI;
import dev.ocean.sigtran.map.parameters.ISDNAddressString;
import dev.ocean.sigtran.map.parameters.MSClassmark2;
import dev.ocean.sigtran.map.parameters.OfferedCamel4Functionalities;
import dev.ocean.sigtran.map.parameters.SupportedCamelPhases;
import dev.ocean.sigtran.map.parameters.UUData;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 */
public class InitialDpArgExtension {

    private ISDNAddressString gmscAddress;
    private CalledPartyNumber forwardingDestinationNumber;
    private MSClassmark2 msClassmakrV2;
    private IMEI imei;
    private SupportedCamelPhases supportedCamelPhases;
    private OfferedCamel4Functionalities offeredCamel4Functionalities;
    private byte[] beareCapability2;
    private ExtBasicServiceCode extBasicServiceCode2;
    private HighLayerCompability highLayerCompability2;
    private byte[] lowLayerCompability;
    private byte[] lowLayerCompability2;
    private Boolean enhancedDialedServicesAllowed = false;
    private UUData uuData;
    private Boolean collectInformationAllowed = false;
    private Boolean releasCallArgExtensionAllowed = false;

    public InitialDpArgExtension() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, AsnException, IOException, IllegalNumberFormatException, UnexpectedDataException {
        aos.writeTag(tagClass, false, tag);

        int lenPos = aos.StartContentDefiniteLength();

        if (gmscAddress != null) {
            this.gmscAddress.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
        }

        if (forwardingDestinationNumber != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            baos.write(this.forwardingDestinationNumber.encode());

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 1);
            aos.writeLength(baos.toByteArray().length);
            aos.write(baos.toByteArray());
        }

        if (msClassmakrV2 != null) {
            msClassmakrV2.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
        }

        if (imei != null) {
            this.imei.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
        }

        if (supportedCamelPhases != null) {
            this.supportedCamelPhases.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
        }

        if (offeredCamel4Functionalities != null) {
            this.offeredCamel4Functionalities.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
        }

        if (beareCapability2 != null) {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 6);
            aos.writeLength(beareCapability2.length);
            aos.write(beareCapability2);
        }

        if (extBasicServiceCode2 != null) {
            this.extBasicServiceCode2.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
        }

        if (highLayerCompability2 != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
            this.highLayerCompability2.encode(baos);

            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 8);
            aos.writeLength(baos.toByteArray().length);
            aos.write(baos.toByteArray());
        }

        if (lowLayerCompability != null) {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 9);
            aos.writeLength(lowLayerCompability.length);
            aos.write(lowLayerCompability);
        }

        if (lowLayerCompability2 != null) {
            aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 10);
            aos.writeLength(lowLayerCompability2.length);
            aos.write(lowLayerCompability2);
        }

        if (enhancedDialedServicesAllowed) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 11);
        }

        if (uuData != null) {
            uuData.encode(Tag.CLASS_CONTEXT_SPECIFIC, 12, aos);
        }

        if (collectInformationAllowed) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 13);
        }

        if (releasCallArgExtensionAllowed) {
            aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 14);
        }
        aos.FinalizeContent(lenPos);
    }

    public void decode(AsnInputStream ais) throws IOException, UnexpectedDataException, IncorrectSyntaxException, IllegalNumberFormatException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            switch (tag) {
                case 0:
                    this.gmscAddress = new ISDNAddressString();
                    this.gmscAddress.decode(ais);
                    break;
                case 1:
                    byte[] data = new byte[ais.readLength()];
                    ais.read(data);

                    this.forwardingDestinationNumber = new CalledPartyNumber();
                    this.forwardingDestinationNumber.decode((data));
                    break;
                case 2:
                    msClassmakrV2 = new MSClassmark2();
                    msClassmakrV2.decode(ais);
                    break;
                case 3:
                    this.imei = new IMEI();
                    this.imei.decode(ais);
                    break;
                case 4:
                    this.supportedCamelPhases = new SupportedCamelPhases();
                    this.supportedCamelPhases.decode(ais);
                    break;
                case 5:
                    this.offeredCamel4Functionalities = new OfferedCamel4Functionalities();
                    this.offeredCamel4Functionalities.decode(ais);
                    break;
                case 6:
                    this.beareCapability2 = new byte[ais.readLength()];
                    ais.read(beareCapability2);
                    break;
                case 7:
                    this.extBasicServiceCode2 = new ExtBasicServiceCode();
                    this.extBasicServiceCode2.decode(ais);
                    break;
                case 8:
                    data = new byte[ais.readLength()];
                    ais.read(data);

                    this.highLayerCompability2 = new HighLayerCompability();
                    this.highLayerCompability2.decode(new ByteArrayInputStream(data));
                    break;
                case 9:
                    this.lowLayerCompability = new byte[ais.readLength()];
                    ais.read(lowLayerCompability);
                    break;
                case 10:
                    this.lowLayerCompability2 = new byte[ais.readLength()];
                    ais.read(lowLayerCompability2);
                    break;
                case 11:
                    this.enhancedDialedServicesAllowed = true;
                    ais.readNull();
                    break;
                case 12:
                    this.uuData = new UUData();
                    this.uuData.decode(ais);
                    break;
                case 13:
                    this.collectInformationAllowed = true;
                    ais.readNull();
                    break;
                case 14:
                    this.releasCallArgExtensionAllowed = true;
                    ais.readNull();
                    break;
            }
        }
    }

    /**
     * @return the gmscAddress
     */
    public ISDNAddressString getGmscAddress() {
        return gmscAddress;
    }

    /**
     * @param gmscAddress the gmscAddress to set
     */
    public void setGmscAddress(ISDNAddressString gmscAddress) {
        this.gmscAddress = gmscAddress;
    }

    /**
     * @return the forwardingDestinationNumber
     */
    public CalledPartyNumber getForwardingDestinationNumber() {
        return forwardingDestinationNumber;
    }

    /**
     * @param forwardingDestinationNumber the forwardingDestinationNumber to set
     */
    public void setForwardingDestinationNumber(CalledPartyNumber forwardingDestinationNumber) {
        this.forwardingDestinationNumber = forwardingDestinationNumber;
    }

    /**
     * @return the msClassmakrV2
     */
    public MSClassmark2 getMsClassmakrV2() {
        return msClassmakrV2;
    }

    /**
     * @param msClassmakrV2 the msClassmakrV2 to set
     */
    public void setMsClassmakrV2(MSClassmark2 msClassmakrV2) {
        this.msClassmakrV2 = msClassmakrV2;
    }

    /**
     * @return the imei
     */
    public IMEI getImei() {
        return imei;
    }

    /**
     * @param imei the imei to set
     */
    public void setImei(IMEI imei) {
        this.imei = imei;
    }

    /**
     * @return the supportedCamelPhases
     */
    public SupportedCamelPhases getSupportedCamelPhases() {
        return supportedCamelPhases;
    }

    /**
     * @param supportedCamelPhases the supportedCamelPhases to set
     */
    public void setSupportedCamelPhases(SupportedCamelPhases supportedCamelPhases) {
        this.supportedCamelPhases = supportedCamelPhases;
    }

    /**
     * @return the offeredCamel4Functionalities
     */
    public OfferedCamel4Functionalities getOfferedCamel4Functionalities() {
        return offeredCamel4Functionalities;
    }

    /**
     * @param offeredCamel4Functionalities the offeredCamel4Functionalities to
     * set
     */
    public void setOfferedCamel4Functionalities(OfferedCamel4Functionalities offeredCamel4Functionalities) {
        this.offeredCamel4Functionalities = offeredCamel4Functionalities;
    }

    /**
     * @return the beareCapability2
     */
    public byte[] getBeareCapability2() {
        return beareCapability2;
    }

    /**
     * @param beareCapability2 the beareCapability2 to set
     */
    public void setBeareCapability2(byte[] beareCapability2) {
        this.beareCapability2 = beareCapability2;
    }

    /**
     * @return the extBasicServiceCode2
     */
    public ExtBasicServiceCode getExtBasicServiceCode2() {
        return extBasicServiceCode2;
    }

    /**
     * @param extBasicServiceCode2 the extBasicServiceCode2 to set
     */
    public void setExtBasicServiceCode2(ExtBasicServiceCode extBasicServiceCode2) {
        this.extBasicServiceCode2 = extBasicServiceCode2;
    }

    /**
     * @return the highLayerCompability2
     */
    public HighLayerCompability getHighLayerCompability2() {
        return highLayerCompability2;
    }

    /**
     * @param highLayerCompability2 the highLayerCompability2 to set
     */
    public void setHighLayerCompability2(HighLayerCompability highLayerCompability2) {
        this.highLayerCompability2 = highLayerCompability2;
    }

    /**
     * @return the lowLayerCompability
     */
    public byte[] getLowLayerCompability() {
        return lowLayerCompability;
    }

    /**
     * @param lowLayerCompability the lowLayerCompability to set
     */
    public void setLowLayerCompability(byte[] lowLayerCompability) {
        this.lowLayerCompability = lowLayerCompability;
    }

    /**
     * @return the lowLayerCompability2
     */
    public byte[] getLowLayerCompability2() {
        return lowLayerCompability2;
    }

    /**
     * @param lowLayerCompability2 the lowLayerCompability2 to set
     */
    public void setLowLayerCompability2(byte[] lowLayerCompability2) {
        this.lowLayerCompability2 = lowLayerCompability2;
    }

    /**
     * @return the enhancedDialedServicesAllowed
     */
    public Boolean getEnhancedDialedServicesAllowed() {
        return enhancedDialedServicesAllowed;
    }

    /**
     * @param enhancedDialedServicesAllowed the enhancedDialedServicesAllowed to
     * set
     */
    public void setEnhancedDialedServicesAllowed(Boolean enhancedDialedServicesAllowed) {
        this.enhancedDialedServicesAllowed = enhancedDialedServicesAllowed;
    }

    /**
     * @return the uuData
     */
    public UUData getUuData() {
        return uuData;
    }

    /**
     * @param uuData the uuData to set
     */
    public void setUuData(UUData uuData) {
        this.uuData = uuData;
    }

    /**
     * @return the collectInformationAllowed
     */
    public Boolean getCollectInformationAllowed() {
        return collectInformationAllowed;
    }

    /**
     * @param collectInformationAllowed the collectInformationAllowed to set
     */
    public void setCollectInformationAllowed(Boolean collectInformationAllowed) {
        this.collectInformationAllowed = collectInformationAllowed;
    }

    /**
     * @return the releasCallArgExtensionAllowed
     */
    public Boolean getReleasCallArgExtensionAllowed() {
        return releasCallArgExtensionAllowed;
    }

    /**
     * @param releasCallArgExtensionAllowed the releasCallArgExtensionAllowed to
     * set
     */
    public void setReleasCallArgExtensionAllowed(Boolean releasCallArgExtensionAllowed) {
        this.releasCallArgExtensionAllowed = releasCallArgExtensionAllowed;
    }
}
