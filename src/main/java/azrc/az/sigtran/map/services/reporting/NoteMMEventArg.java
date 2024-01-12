/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.reporting;

import azrc.az.sigtran.cap.parameters.ServiceKey;
import azrc.az.sigtran.map.parameters.location.information.LocationInformation;
import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.services.common.MAPArgument;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.IMSI;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.map.parameters.LocationInformationGPRS;
import azrc.az.sigtran.map.parameters.MMCode;
import azrc.az.sigtran.map.parameters.OfferedCamel4Functionalities;
import azrc.az.sigtran.map.parameters.SupportedCamelPhases;

import java.io.IOException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author eatakishiyev
 *
 * NoteMM-EventArg::= SEQUENCE {
 * serviceKey ServiceKey,
 * eventMet [0] MM-Code,
 * imsi [1] IMSI,
 * msisdn [2] ISDN-AddressString,
 * locationInformation [3] LocationInformation OPTIONAL,
 * supportedCAMELPhases [5] SupportedCamelPhases OPTIONAL,
 * extensionContainer [6] ExtensionContainer OPTIONAL,
 * ...,
 * locationInformationGPRS [7] LocationInformationGPRS OPTIONAL,
 * offeredCamel4Functionalities [8] OfferedCamel4Functionalities OPTIONAL
 * }
 *
 */
public class NoteMMEventArg implements MAPArgument {

    private ServiceKey serviceKey;
    private MMCode eventMet;
    private IMSI imsi;
    private ISDNAddressString msisdn;
    private LocationInformation locationInformation;
    private SupportedCamelPhases supportedCamelPhases;
    private ExtensionContainer extensionContainer;
    private LocationInformationGPRS locationInformationGPRS;
    private OfferedCamel4Functionalities offeredCamel4Functionalities;

    private byte[] rawData;
    private boolean corruptedArgument = false;

    public NoteMMEventArg(ServiceKey serviceKey, MMCode eventMet, IMSI imsi,
            ISDNAddressString msisdn) {
        this.serviceKey = serviceKey;
        this.eventMet = eventMet;
        this.imsi = imsi;
        this.msisdn = msisdn;
    }

    public NoteMMEventArg() {
    }

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int pos = aos.StartContentDefiniteLength();
            serviceKey.encode(aos);
            aos.writeInteger(Tag.CLASS_CONTEXT_SPECIFIC, 0, eventMet.getValue());
            imsi.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            msisdn.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            if (locationInformation != null) {
                locationInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }

            if (supportedCamelPhases != null) {
                supportedCamelPhases.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }

            if (locationInformationGPRS != null) {
                locationInformationGPRS.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }

            if (offeredCamel4Functionalities != null) {
                offeredCamel4Functionalities.encode(Tag.CLASS_CONTEXT_SPECIFIC, 8, aos);
            }
            aos.FinalizeContent(pos);
        } catch (AsnException | IOException | IllegalNumberFormatException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.rawData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }
            this._decode(ais.readSequenceStream());
        } catch (IOException | AsnException | IllegalNumberFormatException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IOException, AsnException, IncorrectSyntaxException, UnexpectedDataException, IllegalNumberFormatException {
        int tag = ais.readTag();
        if (tag == Tag.INTEGER && ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
            this.serviceKey = new ServiceKey();
            serviceKey.decode(ais);
        } else {
            throw new IncorrectSyntaxException(String.format("Expecting Tag[Integer] Class[UNIVERSAL]."
                    + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
        }

        while (ais.available() > 0) {
            tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new IncorrectSyntaxException(String.format("Expecting Class[CONTEXT]."
                        + "Received Tag[%s] Class[%s]", tag, ais.getTagClass()));
            }
            switch (tag) {
                case 0:
                    this.eventMet = MMCode.getInstance((int) ais.readInteger());
                    break;
                case 1:
                    this.imsi = new IMSI(ais);
                    break;
                case 2:
                    this.msisdn = new ISDNAddressString(ais);
                    break;
                case 3:
                    this.locationInformation = new LocationInformation();
                    locationInformation.decode(ais);
                    break;
                case 5:
                    this.supportedCamelPhases = new SupportedCamelPhases();
                    supportedCamelPhases.decode(ais);
                    break;
                case 6:
                    this.extensionContainer = new ExtensionContainer(ais);
                    break;
                case 7:
                    this.locationInformationGPRS = new LocationInformationGPRS();
                    locationInformationGPRS.decode(ais);
                    break;
                case 8:
                    this.offeredCamel4Functionalities = new OfferedCamel4Functionalities();
                    offeredCamel4Functionalities.decode(ais);
                    break;
            }
        }

    }

    @Override
    public byte[] getRequestData() {
        return rawData;
    }

    @Override
    public boolean isRequestCorrupted() {
        return corruptedArgument;
    }

    public MMCode getEventMet() {
        return eventMet;
    }

    public void setEventMet(MMCode eventMet) {
        this.eventMet = eventMet;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public void setImsi(IMSI imsi) {
        this.imsi = imsi;
    }

    public IMSI getImsi() {
        return imsi;
    }

    public void setLocationInformation(LocationInformation locationInformation) {
        this.locationInformation = locationInformation;
    }

    public LocationInformation getLocationInformation() {
        return locationInformation;
    }

    public void setLocationInformationGPRS(LocationInformationGPRS locationInformationGPRS) {
        this.locationInformationGPRS = locationInformationGPRS;
    }

    public LocationInformationGPRS getLocationInformationGPRS() {
        return locationInformationGPRS;
    }

    public void setMsisdn(ISDNAddressString msisdn) {
        this.msisdn = msisdn;
    }

    public ISDNAddressString getMsisdn() {
        return msisdn;
    }

    public void setOfferedCamel4Functionalities(OfferedCamel4Functionalities offeredCamel4Functionalities) {
        this.offeredCamel4Functionalities = offeredCamel4Functionalities;
    }

    public OfferedCamel4Functionalities getOfferedCamel4Functionalities() {
        return offeredCamel4Functionalities;
    }

    public byte[] getRawData() {
        return rawData;
    }

    public void setServiceKey(ServiceKey serviceKey) {
        this.serviceKey = serviceKey;
    }

    public void setSupportedCamelPhases(SupportedCamelPhases supportedCamelPhases) {
        this.supportedCamelPhases = supportedCamelPhases;
    }

    public SupportedCamelPhases getSupportedCamelPhases() {
        return supportedCamelPhases;
    }

    public boolean isCorruptedArgument() {
        return corruptedArgument;
    }

}
