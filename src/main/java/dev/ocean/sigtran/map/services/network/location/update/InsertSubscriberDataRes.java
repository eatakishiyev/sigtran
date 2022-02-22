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
import dev.ocean.sigtran.map.parameters.BearerServiceList;
import dev.ocean.sigtran.map.parameters.ExtensionContainer;
import dev.ocean.sigtran.map.parameters.ODBGeneralData;
import dev.ocean.sigtran.map.parameters.OfferedCamel4CSIs;
import dev.ocean.sigtran.map.parameters.RegionalSubscriptionResponse;
import dev.ocean.sigtran.map.parameters.SSList;
import dev.ocean.sigtran.map.parameters.SupportedCamelPhases;
import dev.ocean.sigtran.map.parameters.SupportedFeatures;
import dev.ocean.sigtran.map.parameters.TeleServiceList;
import dev.ocean.sigtran.map.services.common.MAPResponse;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;

/**
 * InsertSubscriberDataRes ::= SEQUENCE {
 * teleserviceList [1] TeleserviceList OPTIONAL,
 * bearerServiceList [2] BearerServiceList OPTIONAL,
 * ss-List [3] SS-List OPTIONAL,
 * odb-GeneralData [4] ODB-GeneralData OPTIONAL,
 * regionalSubscriptionResponse [5] RegionalSubscriptionResponse OPTIONAL,
 * supportedCamelPhases [6] SupportedCamelPhases OPTIONAL,
 * extensionContainer [7] ExtensionContainer OPTIONAL,
 * ... ,
 * offeredCamel4CSIs [8] OfferedCamel4CSIs OPTIONAL,
 * supportedFeatures [9] SupportedFeatures OPTIONAL }
 *
 * @author eatakishiyev
 */
public class InsertSubscriberDataRes implements MAPResponse {

    private TeleServiceList teleServiceList;
    private BearerServiceList bearerServiceList;
    private SSList ssList;
    private ODBGeneralData odbGeneralData;
    private RegionalSubscriptionResponse regionalSubscriptionResponse;
    private SupportedCamelPhases supportedCamelPhases;
    private ExtensionContainer extensionContainer;
    private OfferedCamel4CSIs offeredCamel4CSISs;
    private SupportedFeatures supportedFeatures;
    private byte[] responseData;
    protected boolean responseCorrupted = false;

    @Override
    public void encode(AsnOutputStream aos) throws IncorrectSyntaxException, UnexpectedDataException {
        try {
            aos.writeTag(Tag.CLASS_UNIVERSAL, false, Tag.SEQUENCE);
            int lenPos = aos.StartContentDefiniteLength();

            if (teleServiceList != null) {
                teleServiceList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 1, aos);
            }

            if (bearerServiceList != null) {
                bearerServiceList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 2, aos);
            }

            if (ssList != null) {
                ssList.encode(Tag.CLASS_CONTEXT_SPECIFIC, 3, aos);
            }

            if (odbGeneralData != null) {
                odbGeneralData.encode(Tag.CLASS_CONTEXT_SPECIFIC, 4, aos);
            }

            if (regionalSubscriptionResponse != null) {
                regionalSubscriptionResponse.encode(Tag.CLASS_CONTEXT_SPECIFIC, 5, aos);
            }

            if (supportedCamelPhases != null) {
                supportedCamelPhases.encode(Tag.CLASS_CONTEXT_SPECIFIC, 6, aos);
            }

            if (extensionContainer != null) {
                extensionContainer.encode(Tag.CLASS_CONTEXT_SPECIFIC, 7, aos);
            }

            if (offeredCamel4CSISs != null) {
                offeredCamel4CSISs.encode(Tag.CLASS_CONTEXT_SPECIFIC, 8, aos);
            }

            if (supportedFeatures != null) {
                supportedFeatures.encode(Tag.CLASS_CONTEXT_SPECIFIC, 9, aos);
            }

            aos.FinalizeContent(lenPos);
        } catch (AsnException | IOException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    @Override
    public void decode(byte[] data) throws IncorrectSyntaxException, UnexpectedDataException {
        this.responseData = data;
        AsnInputStream ais = new AsnInputStream(data);
        try {
            int tag = ais.readTag();
            if (tag != Tag.SEQUENCE && ais.getTagClass() != Tag.CLASS_UNIVERSAL) {
                throw new IncorrectSyntaxException(String.format("Received unexpected tag. Expecting Tag[SEQUENCE] TagClass[UNIVERSAL]"
                        + " found Tag[%s] TagClass[%s]", tag, ais.getTagClass()));
            }

            this._decode(ais.readSequenceStream());
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex);
        }
    }

    private void _decode(AsnInputStream ais) throws IOException, IncorrectSyntaxException, UnexpectedDataException, AsnException {
        while (ais.available() > 0) {
            int tag = ais.readTag();
            if (ais.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                throw new IncorrectSyntaxException(String.format("Unexpected tag Class[%s] received. Expecting Class[CONTEXT].", ais.getTagClass()));
            }
            switch (tag) {
                case 1:
                    this.teleServiceList = new TeleServiceList();
                    teleServiceList.decode(ais.readSequenceStream());
                    break;
                case 2:
                    this.bearerServiceList = new BearerServiceList();
                    bearerServiceList.decode(ais.readSequenceStream());
                    break;
                case 3:
                    this.ssList = new SSList();
                    ssList.decode(ais.readSequenceStream());
                    break;
                case 4:
                    this.odbGeneralData = new ODBGeneralData();
                    odbGeneralData.decode(ais);
                    break;
                case 5:
                    this.regionalSubscriptionResponse = new RegionalSubscriptionResponse();
                    regionalSubscriptionResponse.decode(ais);
                    break;
                case 6:
                    this.supportedCamelPhases = new SupportedCamelPhases();
                    supportedCamelPhases.decode(ais);
                    break;
                case 7:
                    this.extensionContainer = new ExtensionContainer();
                    extensionContainer.decode(ais);
                    break;
                case 8:
                    this.offeredCamel4CSISs = new OfferedCamel4CSIs();
                    offeredCamel4CSISs.decode(ais);
                    break;
                case 9:
                    this.supportedFeatures = new SupportedFeatures();
                    supportedFeatures.decode(ais);
                    break;
                default:
                    throw new IncorrectSyntaxException(String.format("Unexpected Tag[%s] received.", tag));
            }
        }
    }

    /**
     * @return the teleServiceList
     */
    public TeleServiceList getTeleServiceList() {
        return teleServiceList;
    }

    /**
     * @param teleServiceList the teleServiceList to set
     */
    public void setTeleServiceList(TeleServiceList teleServiceList) {
        this.teleServiceList = teleServiceList;
    }

    /**
     * @return the bearerServiceList
     */
    public BearerServiceList getBearerServiceList() {
        return bearerServiceList;
    }

    /**
     * @param bearerServiceList the bearerServiceList to set
     */
    public void setBearerServiceList(BearerServiceList bearerServiceList) {
        this.bearerServiceList = bearerServiceList;
    }

    /**
     * @return the ssList
     */
    public SSList getSsList() {
        return ssList;
    }

    /**
     * @param ssList the ssList to set
     */
    public void setSsList(SSList ssList) {
        this.ssList = ssList;
    }

    /**
     * @return the odbGeneralData
     */
    public ODBGeneralData getOdbGeneralData() {
        return odbGeneralData;
    }

    /**
     * @param odbGeneralData the odbGeneralData to set
     */
    public void setOdbGeneralData(ODBGeneralData odbGeneralData) {
        this.odbGeneralData = odbGeneralData;
    }

    /**
     * @return the regionalSubscriptionResponse
     */
    public RegionalSubscriptionResponse getRegionalSubscriptionResponse() {
        return regionalSubscriptionResponse;
    }

    /**
     * @param regionalSubscriptionResponse the regionalSubscriptionResponse to
     * set
     */
    public void setRegionalSubscriptionResponse(RegionalSubscriptionResponse regionalSubscriptionResponse) {
        this.regionalSubscriptionResponse = regionalSubscriptionResponse;
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

    public void setSupportedCamelPhases(boolean phase1, boolean phase2, boolean phase3, boolean phase4) {
        this.supportedCamelPhases = new SupportedCamelPhases(phase1, phase2, phase3, phase4);
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
     * @return the offeredCamel4CSISs
     */
    public OfferedCamel4CSIs getOfferedCamel4CSISs() {
        return offeredCamel4CSISs;
    }

    /**
     * @param offeredCamel4CSISs the offeredCamel4CSISs to set
     */
    public void setOfferedCamel4CSISs(OfferedCamel4CSIs offeredCamel4CSISs) {
        this.offeredCamel4CSISs = offeredCamel4CSISs;
    }

    /**
     * @return the supportedFeatures
     */
    public SupportedFeatures getSupportedFeatures() {
        return supportedFeatures;
    }

    /**
     * @param supportedFeatures the supportedFeatures to set
     */
    public void setSupportedFeatures(SupportedFeatures supportedFeatures) {
        this.supportedFeatures = supportedFeatures;
    }

    public byte[] getResponseData() {
        return responseData;
    }

    public boolean isResponseCorrupted() {
        return responseCorrupted;
    }

}
