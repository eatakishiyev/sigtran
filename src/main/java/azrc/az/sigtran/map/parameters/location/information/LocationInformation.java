/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.parameters.location.information;

import azrc.az.isup.parameters.CallingGeodeticLocation;
import azrc.az.isup.parameters.LocationNumber;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import azrc.az.sigtran.common.exceptions.IllegalNumberFormatException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import azrc.az.sigtran.map.parameters.AgeOfLocationInformation;
import azrc.az.sigtran.map.parameters.CellGlobalIdOrServiceAreaIdOrLAI;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.ISDNAddressString;
import azrc.az.sigtran.map.parameters.LSAIdentity;
import azrc.az.sigtran.map.parameters.UserCSGInformation;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * LocationInformation ::= SEQUENCE { ageOfLocationInformation AgeOfLocationInformation OPTIONAL,
 * geographicalInformation [0] GeographicalInformation OPTIONAL,
 * vlr-number [1] ISDN-AddressString OPTIONAL,
 * locationNumber [2] LocationNumber OPTIONAL,
 * cellGlobalIdOrServiceAreaIdOrLAI [3] CellGlobalIdOrServiceAreaIdOrLAI OPTIONAL,
 * extensionContainer [4] ExtensionContainer OPTIONAL, ... ,
 * selectedLSA-Id [5] LSAIdentity OPTIONAL,
 * msc-Number [6] ISDN-AddressString OPTIONAL,
 * geodeticInformation [7] CallingGeodeticLocation OPTIONAL,
 * currentLocationRetrieved [8] NULL OPTIONAL,
 * sai-Present [9] NULL OPTIONAL,
 * locationInformationEPS [10] LocationInformationEPS OPTIONAL,
 * userCSGInformation [11] UserCSGInformation OPTIONAL }
 * -- sai-Present indicates that the cellGlobalIdOrServiceAreaIdOrLAI
 * parameter contains -- a Service Area Identity. -- currentLocationRetrieved
 * shall be present -- if the location information were retrieved after a
 * successfull paging. -- if the locationinformationEPS IE is present then the
 * cellGlobalIdOrServiceAreaIdOrLAI IE, -- the ageOfLocationInformation IE, the
 * geographicalInformation IE, the geodeticInformation IE -- and the
 * currentLocationRetrieved IE (outside the locationInformationEPS IE) shall be
 * -- absent. -- UserCSGInformation contains the CSG ID, Access mode, and the
 * CSG Membership Indication in -- the case the Access mode is Hybrid Mode. --
 * The locationInformationEPS IE should be absent if
 * locationInformationEPS-Supported was not -- received in the RequestedInfo IE.
 *
 * @author eatakishiyev
 */
public class LocationInformation implements Serializable {

    private AgeOfLocationInformation ageOfLocationInformation;
    private GeographicalInformation geographicalInformation;
    private ISDNAddressString vlrNumber;
    private LocationNumber locationNumber;
    private CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI;
    private ExtensionContainer extensionContainer;
    private LSAIdentity selectedLSAId;
    private ISDNAddressString mscNumber;
    private CallingGeodeticLocation geodeticInformation;
    private boolean currentLocationRetrieved = false;
    private boolean saiPresent = false;
    private LocationInformationEPS locationInformationEPS;
    private UserCSGInformation userCSGInformation;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LocationInformation[")
                .append(";AgeOfLocationInformation = ").append(ageOfLocationInformation).append("\n")
                .append(";GeographicalInformation = ").append(geographicalInformation).append("\n")
                .append(";VLRNumber = ").append(vlrNumber).append("\n")
                .append(";LocationNumber = ").append(locationNumber).append("\n")
                .append(";CellGlobalIdOrServiceAreaIdOrLAI = ").append(cellGlobalIdOrServiceAreaIdOrLAI).append("\n")
                .append(";SelectedLSAID = ").append(selectedLSAId).append("\n")
                .append(";MSCNumber = ").append(mscNumber).append("\n")
                .append(";GeodeticInformation = ").append(geodeticInformation).append("\n")
                .append(";CurrentLocationretrieved = ").append(currentLocationRetrieved).append("\n")
                .append(";SAIPresent = ").append(saiPresent).append("\n")
                .append(";LocationInformationEPS = ").append(locationInformationEPS).append("\n")
                .append(";UserCSGInformation = ").append(userCSGInformation).append("\n")
                .append("]");
        return sb.toString();
    }

    public LocationInformation() {
    }

    public void encode(int tagClass, int tag, AsnOutputStream aos) throws IllegalNumberFormatException, IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(tagClass, false, tag);
            int lenPos = aos.StartContentDefiniteLength();

            if (this.ageOfLocationInformation != null) {
                ageOfLocationInformation.encode(Tag.INTEGER, Tag.CLASS_UNIVERSAL, aos);
            }

            if (this.geographicalInformation != null) {
                this.geographicalInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 0, aos);
            }

            if (this.vlrNumber != null) {
                this.vlrNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            if (this.locationNumber != null) {
                aos.writeTag(Tag.CLASS_CONTEXT_SPECIFIC, true, 2);

                ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
                this.locationNumber.encode(baos);

                aos.writeLength(baos.toByteArray().length);
                aos.write(baos.toByteArray());
            }

            if (this.cellGlobalIdOrServiceAreaIdOrLAI != null) {
                this.cellGlobalIdOrServiceAreaIdOrLAI.encode(3, Tag.CLASS_CONTEXT_SPECIFIC, aos);
            }

            if (this.extensionContainer != null) {
                this.extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }

            if (this.selectedLSAId != null) {
                this.selectedLSAId.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }

            if (this.mscNumber != null) {
                this.mscNumber.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }

            if (this.geodeticInformation != null) {
                this.geodeticInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }

            if (currentLocationRetrieved) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 8);
            }

            if (saiPresent) {
                aos.writeNull(Tag.CLASS_CONTEXT_SPECIFIC, 9);
            }

            if (locationInformationEPS != null) {
                locationInformationEPS.encode(Tag.CLASS_CONTEXT_SPECIFIC, 10, aos);
            }

            if (userCSGInformation != null) {
                userCSGInformation.encode(Tag.CLASS_CONTEXT_SPECIFIC, 11, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    public void decode(AsnInputStream ais) throws IOException, IncorrectSyntaxException, AsnException, UnexpectedDataException, IllegalNumberFormatException {
        AsnInputStream tmpAis = ais.readSequenceStream();
        this.decode_(tmpAis);
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

    public void setAgeOfLocationInformation(int ageOfLocationInformation) {
        this.ageOfLocationInformation = new AgeOfLocationInformation(ageOfLocationInformation);
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
     * @return the locationNumber
     */
    public LocationNumber getLocationNumber() {
        return locationNumber;
    }

    /**
     * @param locationNumber the locationNumber to set
     */
    public void setLocationNumber(LocationNumber locationNumber) {
        this.locationNumber = locationNumber;
    }

    /**
     * @return the cellGlobalIdOrServiceAreaIdOrLAI
     */
    public CellGlobalIdOrServiceAreaIdOrLAI getCellGlobalIdOrServiceAreaIdOrLAI() {
        return cellGlobalIdOrServiceAreaIdOrLAI;
    }

    /**
     * @param cellGlobalIdOrServiceAreaIdOrLAI the
     * cellGlobalIdOrServiceAreaIdOrLAI to set
     */
    public void setCellGlobalIdOrServiceAreaIdOrLAI(CellGlobalIdOrServiceAreaIdOrLAI cellGlobalIdOrServiceAreaIdOrLAI) {
        this.cellGlobalIdOrServiceAreaIdOrLAI = cellGlobalIdOrServiceAreaIdOrLAI;
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
     * @return the selectedLSAId
     */
    public LSAIdentity getSelectedLSAId() {
        return selectedLSAId;
    }

    /**
     * @param selectedLSAId the selectedLSAId to set
     */
    public void setSelectedLSAId(LSAIdentity selectedLSAId) {
        this.selectedLSAId = selectedLSAId;
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
    public Boolean getCurrentLocationRetrieved() {
        return currentLocationRetrieved;
    }

    /**
     * @param currentLocationRetrieved the currentLocationRetrieved to set
     */
    public void setCurrentLocationRetrieved(Boolean currentLocationRetrieved) {
        this.currentLocationRetrieved = currentLocationRetrieved;
    }

    public LocationInformationEPS getLocationInformationEPS() {
        return locationInformationEPS;
    }

    public void setCurrentLocationRetrieved(boolean currentLocationRetrieved) {
        this.currentLocationRetrieved = currentLocationRetrieved;
    }

    public UserCSGInformation getUserCSGInformation() {
        return userCSGInformation;
    }

    public void setLocationInformationEPS(LocationInformationEPS locationInformationEPS) {
        this.locationInformationEPS = locationInformationEPS;
    }

    public void setSaiPresent(boolean saiPresent) {
        this.saiPresent = saiPresent;
    }

    private void decode_(AsnInputStream ais) throws IOException, UnexpectedDataException, IncorrectSyntaxException, AsnException, IllegalNumberFormatException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            switch (tag) {
                case 0:
                    this.geographicalInformation = new GeographicalInformation();
                    this.geographicalInformation.decode(ais);
                    break;
                case 1:
                    this.vlrNumber = new ISDNAddressString();
                    this.vlrNumber.decode(ais);
                    break;
                case 2:
                    if (ais.getTagClass() == Tag.CLASS_UNIVERSAL) {
                        this.ageOfLocationInformation = new AgeOfLocationInformation();
                        this.ageOfLocationInformation.decode(ais);
                    } else {
                        this.locationNumber = new LocationNumber();
                        this.locationNumber.decode(new ByteArrayInputStream(ais.readOctetString()));
                    }
                    break;
                case 3:
                    this.cellGlobalIdOrServiceAreaIdOrLAI = new CellGlobalIdOrServiceAreaIdOrLAI();
                    this.cellGlobalIdOrServiceAreaIdOrLAI.decode(ais);
                    break;
                case 4:
                    this.extensionContainer = new ExtensionContainer();
                    this.extensionContainer.decode(ais);
                    break;
                case 5:
                    this.selectedLSAId = new LSAIdentity();
                    this.selectedLSAId.decode(ais);
                    break;
                case 6:
                    this.mscNumber = new ISDNAddressString();
                    this.mscNumber.decode(ais);
                    break;
                case 7:
                    this.geodeticInformation = new CallingGeodeticLocation();
                    this.geodeticInformation.decode(ais);
                    break;
                case 8:
                    this.currentLocationRetrieved = true;
                    ais.readNull();
                    break;
                case 9:
                    this.saiPresent = true;
                    ais.readNull();
                    break;
                case 10:
                    locationInformationEPS = new LocationInformationEPS();
                    locationInformationEPS.decode(ais);
                    break;
                case 11:
                    userCSGInformation = new UserCSGInformation();
                    userCSGInformation.decode(ais);
                    break;
                default:
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag. Expecting Class[CONTEXT], found Tag[%d] Class[%d]", tag, ais.getTagClass()));
            }
        }
    }
}
