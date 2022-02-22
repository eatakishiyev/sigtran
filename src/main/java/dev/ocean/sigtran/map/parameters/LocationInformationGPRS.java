/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.parameters;

import java.io.IOException;
import dev.ocean.sigtran.common.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.isup.parameters.CallingGeodeticLocation;
import dev.ocean.sigtran.map.parameters.location.information.GeographicalInformation;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * LocationInformationGPRS ::= SEQUENCE { cellGlobalIdOrServiceAreaIdOrLAI [0]
 CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL, routeingAreaIdentity [1]
 RAIdentity OPTIONAL, geographicalInformation [2] GeographicalInformation
 OPTIONAL, sgsn-Number [3] ISDN-AddressString OPTIONAL, selectedLSAIdentity
 [4] LSAIdentity OPTIONAL, extensionContainer [5] ExtensionContainer OPTIONAL,
 ..., sai-Present [6] NULL OPTIONAL, geodeticInformation [7]
 CallingGeodeticLocation OPTIONAL, currentLocationRetrieved [8] NULL OPTIONAL,
 ageOfLocationInformation [9] AgeOfLocationInformation OPTIONAL,
 userCSGInformation [10] UserCSGInformation OPTIONAL } -- sai-Present
 indicates that the cellGlobalIdOrServiceAreaIdOrLAI parameter contains -- a
 Service Area Identity. -- currentLocationRetrieved shall be present if the
 * location information -- was retrieved after successful paging. --
 * UserCSGInformation contains the CSG ID, Access mode, and the CSG Membership
 * Indication in -- the case the Access mode is Hybrid Mode.
 *
 * @author eatakishiyev
 */
public class LocationInformationGPRS {

    private CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLai;
    private RAIdentity routeingAreaIdentity;
    private GeographicalInformation geographicalInformation;
    private ISDNAddressString sgsnNumber;
    private LSAIdentity selectedLSAIdentity;
    private ExtensionContainer extensionContainer;
    private boolean saiPresent;
    private CallingGeodeticLocation geodeticInformation;
    private boolean currentLocationRetrieved;
    private AgeOfLocationInformation ageOfLocationInformation;
    private UserCSGInformation userCSGInformation;

    public LocationInformationGPRS() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();
            if (cellGlobalIdOrServiceAreaIdOrLai != null) {
                cellGlobalIdOrServiceAreaIdOrLai.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }
            if (routeingAreaIdentity != null) {
                routeingAreaIdentity.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }
            if (geographicalInformation != null) {
                geographicalInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }
            if (sgsnNumber != null) {
                sgsnNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }
            if (selectedLSAIdentity != null) {
                selectedLSAIdentity.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }
            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }
            if (saiPresent) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 6);
            }
            if (geodeticInformation != null) {
                geodeticInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }
            if (currentLocationRetrieved) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 8);
            }
            if (ageOfLocationInformation != null) {
                ageOfLocationInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 9, aos);
            }
            if (userCSGInformation != null) {
                userCSGInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 10, aos);
            }
            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        this.encode(Tag.CLASS_UNIVERSAL, Tag.SEQUENCE, aos);
    }

    public void decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            this._decode(ais.readSequenceStream());
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            while (ais.available() > 0) {
                int tag = ais.readTag();
                if (tag == 0 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.cellGlobalIdOrServiceAreaIdOrLai = new CellGlobalIdOrServiceAreaIdOrLAI();
                    cellGlobalIdOrServiceAreaIdOrLai.decode(ais);
                } else if (tag == 1 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.routeingAreaIdentity = new RAIdentity();
                    routeingAreaIdentity.decode(ais);
                } else if (tag == 2 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.geographicalInformation = new GeographicalInformation();
                    geographicalInformation.decode(ais);
                } else if (tag == 3 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.sgsnNumber = new ISDNAddressString(ais);
                } else if (tag == 4 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.selectedLSAIdentity = new LSAIdentity();
                    selectedLSAIdentity.decode(ais);
                } else if (tag == 5 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    extensionContainer = new ExtensionContainer();
                    extensionContainer.decode(ais);
                } else if (tag == 6 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.saiPresent = true;
                    ais.readNull();
                } else if (tag == 7 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.geodeticInformation = new CallingGeodeticLocation();
                    geodeticInformation.decode(ais);
                } else if (tag == 8 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.currentLocationRetrieved = true;
                    ais.readNull();
                } else if (tag == 9 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.ageOfLocationInformation = new AgeOfLocationInformation();
                    ageOfLocationInformation.decode(ais);
                } else if (tag == 10 && ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    this.userCSGInformation = new UserCSGInformation();
                    userCSGInformation.decode(ais);
                } else {
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] Class[%s] received.", tag, ais.getTagClass()));
                }
            }
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    /**
     * @return the cellGlobalIdOrServiceAreaIdOrLai
     */
    public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLai() {
        return cellGlobalIdOrServiceAreaIdOrLai;
    }

    /**
     * @param cellGlobalIdOrServiceAreaIdOrLai the
     * cellGlobalIdOrServiceAreaIdOrLai to set
     */
    public void setCellGlobalIdOrServiceAreaIdOrLai(CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLai) {
        this.cellGlobalIdOrServiceAreaIdOrLai = cellGlobalIdOrServiceAreaIdOrLai;
    }

    /**
     * @return the routeingAreaIdentity
     */
    public RAIdentity getRouteingAreaIdentity() {
        return routeingAreaIdentity;
    }

    /**
     * @param routeingAreaIdentity the routeingAreaIdentity to set
     */
    public void setRouteingAreaIdentity(RAIdentity routeingAreaIdentity) {
        this.routeingAreaIdentity = routeingAreaIdentity;
    }

    /**
     * @return the geographicalInformation
     */
    public GeographicalInformation getGeographicalInformation() {
        return geographicalInformation;
    }

    /**
     * @param geographicalInformation the geographicalInformation to set
     */
    public void setGeographicalInformation(GeographicalInformation geographicalInformation) {
        this.geographicalInformation = geographicalInformation;
    }

    /**
     * @return the sgsnNumber
     */
    public ISDNAddressString getSgsnNumber() {
        return sgsnNumber;
    }

    /**
     * @param sgsnNumber the sgsnNumber to set
     */
    public void setSgsnNumber(ISDNAddressString sgsnNumber) {
        this.sgsnNumber = sgsnNumber;
    }

    /**
     * @return the selectedLSAIdentity
     */
    public LSAIdentity getSelectedLSAIdentity() {
        return selectedLSAIdentity;
    }

    /**
     * @param selectedLSAIdentity the selectedLSAIdentity to set
     */
    public void setSelectedLSAIdentity(LSAIdentity selectedLSAIdentity) {
        this.selectedLSAIdentity = selectedLSAIdentity;
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
     * @return the saiPresent
     */
    public boolean isSaiPresent() {
        return saiPresent;
    }

    /**
     * @param saiPresent the saiPresent to set
     */
    public void setSaiPresent(boolean saiPresent) {
        this.saiPresent = saiPresent;
    }

    /**
     * @return the geodeticInformation
     */
    public CallingGeodeticLocation getGeodeticInformation() {
        return geodeticInformation;
    }

    /**
     * @param geodeticInformation the geodeticInformation to set
     */
    public void setGeodeticInformation(CallingGeodeticLocation geodeticInformation) {
        this.geodeticInformation = geodeticInformation;
    }

    /**
     * @return the currentLocationRetrieved
     */
    public boolean isCurrentLocationRetrieved() {
        return currentLocationRetrieved;
    }

    /**
     * @param currentLocationRetrieved the currentLocationRetrieved to set
     */
    public void setCurrentLocationRetrieved(boolean currentLocationRetrieved) {
        this.currentLocationRetrieved = currentLocationRetrieved;
    }

    /**
     * @return the userCSGInformation
     */
    public UserCSGInformation getUserCSGInformation() {
        return userCSGInformation;
    }

    /**
     * @param userCSGInformation the userCSGInformation to set
     */
    public void setUserCSGInformation(UserCSGInformation userCSGInformation) {
        this.userCSGInformation = userCSGInformation;
    }

    /**
     * @return the ageOfLocationInformation
     */
    public AgeOfLocationInformation getAgeOfLocationInformation() {
        return ageOfLocationInformation;
    }

    /**
     * @param ageOfLocationInformation the ageOfLocationInformation to set
     */
    public void setAgeOfLocationInformation(AgeOfLocationInformation ageOfLocationInformation) {
        this.ageOfLocationInformation = ageOfLocationInformation;
    }

}
