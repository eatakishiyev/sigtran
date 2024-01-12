/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters.location.information;

import azrc.az.isup.parameters.CallingGeodeticLocation;

import java.io.IOException;
import java.io.Serializable;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.parameters.AgeOfLocationInformation;
import azrc.az.sigtran.map.parameters.DiameterIdentity;
import azrc.az.sigtran.map.parameters.EUtranCGI;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.TAId;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * LocationInformationEPS ::= SEQUENCE { e-utranCellGlobalIdentity [0]
 E-UTRAN-CGI OPTIONAL, trackingAreaIdentity [1] TA-Id OPTIONAL,
 extensionContainer [2] ExtensionContainer OPTIONAL, geographicalInformation
 [3] GeographicalInformation OPTIONAL, geodeticInformation [4]
 CallingGeodeticLocation OPTIONAL, currentLocationRetrieved [5] NULL OPTIONAL,
 ageOfLocationInformation [6] AgeOfLocationInformation OPTIONAL, ..., mme-Name
 [7] DiameterIdentity OPTIONAL } -- currentLocationRetrieved shall be present
 if the location information -- was retrieved after successful paging.
 *
 * @author eatakishiyev
 */
public class LocationInformationEPS implements Serializable {

    private EUtranCGI eUtranCellGlobalIdentity;
    private TAId trackingAreaIdentity;
    private ExtensionContainer extensionContainer;
    private GeographicalInformation geographicalInformation;
    private CallingGeodeticLocation geodeticInformation;
    private boolean currentLocationRetrieved;
    private AgeOfLocationInformation ageOfLocationInformation;
    private DiameterIdentity mmeName;

    public LocationInformationEPS() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int pos = aos.StartContentDefiniteLength();
            if (eUtranCellGlobalIdentity != null) {
                eUtranCellGlobalIdentity.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }
            if (trackingAreaIdentity != null) {
                trackingAreaIdentity.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            if (geographicalInformation != null) {
                geographicalInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }
            if (geodeticInformation != null) {
                geodeticInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }
            if (currentLocationRetrieved) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 5);
            }
            if (ageOfLocationInformation != null) {
                ageOfLocationInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }
            if (mmeName != null) {
                mmeName.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }
            aos.FinalizeContent(pos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            AsnInputStream _ais = ais.readSequenceStream();
            while (_ais.available() > 0) {
                int tag = _ais.readTag();
                if (_ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new IncorrectSyntaxException(String.format("Unexpected TagClass. Expecting Class[CONTEXT], found Tag[%d] Class[%d]", tag, _ais.getTagClass()));
                }

                switch (tag) {
                    case 0:
                        eUtranCellGlobalIdentity = new EUtranCGI();
                        eUtranCellGlobalIdentity.decode(_ais);
                        break;
                    case 1:
                        trackingAreaIdentity = new TAId();
                        trackingAreaIdentity.decode(_ais);
                        break;
                    case 2:
                        extensionContainer = new ExtensionContainer(_ais);
                        break;
                    case 3:
                        geographicalInformation = new GeographicalInformation();
                        geographicalInformation.decode(_ais);
                        break;
                    case 4:
                        geodeticInformation = new CallingGeodeticLocation();
                        geodeticInformation.decode(_ais);
                        break;
                    case 5:
                        currentLocationRetrieved = true;
                        _ais.readNull();
                        break;
                    case 6:
                        ageOfLocationInformation = new AgeOfLocationInformation();
                        ageOfLocationInformation.decode(_ais);
                        break;
                    case 7:
                        mmeName = new DiameterIdentity(_ais);
                        break;
                    default:
                        throw new IncorrectSyntaxException(String.format("Unexpected Tag. Expecting Class[CONTEXT], found Tag[%d] Class[%d]", tag, _ais.getTagClass()));
                }
            }
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public AgeOfLocationInformation getAgeOfLocationInformation() {
        return ageOfLocationInformation;
    }

    public void setAgeOfLocationInformation(AgeOfLocationInformation ageOfLocationInformation) {
        this.ageOfLocationInformation = ageOfLocationInformation;
    }

    public ExtensionContainer getExtensionContainer() {
        return extensionContainer;
    }

    public void setExtensionContainer(ExtensionContainer extensionContainer) {
        this.extensionContainer = extensionContainer;
    }

    public CallingGeodeticLocation getGeodeticInformation() {
        return geodeticInformation;
    }

    public void setGeodeticInformation(CallingGeodeticLocation geodeticInformation) {
        this.geodeticInformation = geodeticInformation;
    }

    public GeographicalInformation getGeographicalInformation() {
        return geographicalInformation;
    }

    public void setGeographicalInformation(GeographicalInformation geographicalInformation) {
        this.geographicalInformation = geographicalInformation;
    }

    public void setCurrentLocationRetrieved(boolean currentLocationRetrieved) {
        this.currentLocationRetrieved = currentLocationRetrieved;
    }

    public DiameterIdentity getMmeName() {
        return mmeName;
    }

    public void setMmeName(DiameterIdentity mmeName) {
        this.mmeName = mmeName;
    }

    public TAId getTrackingAreaIdentity() {
        return trackingAreaIdentity;
    }

    public void setTrackingAreaIdentity(TAId trackingAreaIdentity) {
        this.trackingAreaIdentity = trackingAreaIdentity;
    }

    public EUtranCGI geteUtranCellGlobalIdentity() {
        return eUtranCellGlobalIdentity;
    }

    public void seteUtranCellGlobalIdentity(EUtranCGI eUtranCellGlobalIdentity) {
        this.eUtranCellGlobalIdentity = eUtranCellGlobalIdentity;
    }

    public boolean isCurrentLocationRetrieved() {
        return currentLocationRetrieved;
    }

}
