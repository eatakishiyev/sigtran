/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.address;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import azrc.az.sigtran.sccp.access.point.MTPServiceAccessPoint;
import azrc.az.sigtran.sccp.general.Encodable;
import azrc.az.sigtran.sccp.globaltitle.GT0001;
import azrc.az.sigtran.sccp.globaltitle.GT0010;
import azrc.az.sigtran.sccp.globaltitle.GT0011;
import azrc.az.sigtran.sccp.globaltitle.GT0100;
import azrc.az.sigtran.sccp.globaltitle.GlobalTitle;
import lombok.Data;

/**
 *
 * @author root
 */
@Data
public class SCCPAddress implements Encodable {

    private AddressIndicator addressIndicator;
    private SubSystemNumber subSystemNumber;
    private GlobalTitle globalTitle;
    private SignallingPointCode signallingPointCode;
    private MTPServiceAccessPoint mtpSap = null;//Assign value after translation successfull.
    private boolean translated = false;

    public SCCPAddress() {
    }

    public SCCPAddress(SubSystemNumber subSystemNumber) {
        this.subSystemNumber = subSystemNumber;
    }

    /**
     *
     * @param routingIndicator
     * @param spc Signalling point code. null means no signalling point code
     * @param globalTitle GlobalTitle. null means no global title included
     * @param subSystemNumber SubsystemNumber. null means no subsystem included
     */
    public SCCPAddress(RoutingIndicator routingIndicator, Integer spc, GlobalTitle globalTitle, SubSystemNumber subSystemNumber) {
        this.addressIndicator = new AddressIndicator(routingIndicator);
        this.addressIndicator.setPointCodeIndicator(false);

        if (spc != null) {
            this.signallingPointCode = new SignallingPointCode(spc);
            this.addressIndicator.setPointCodeIndicator(true);
        }

//        if (routingIndicator == RoutingIndicator.ROUTE_ON_SSN && spc != null) {
//            this.signallingPointCode = new SignallingPointCode(spc);
//            this.addressIndicator.setPointCodeIndicator(true);
//        }
        this.addressIndicator.setGlobalTitleIndicator(globalTitle == null ? GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED : globalTitle.getGlobalTitleIndicator());
        this.addressIndicator.setSsnIndicator(subSystemNumber != null);
        this.globalTitle = globalTitle;
        this.subSystemNumber = subSystemNumber;
    }

    public SCCPAddress(RoutingIndicator routingIndicator, GlobalTitle globalTitle,
            SubSystemNumber ssn) {
        this(routingIndicator, null, globalTitle, ssn);
    }

    public SCCPAddress(byte[] data) throws Exception {
        if (data == null) {
            throw new NullPointerException("Parameter can not be null");
        }
        if (data.length == 0) {
            throw new Exception("Incorrect parameter length");
        }
        this.decode(new ByteArrayInputStream(data));
    }

    @Override
    public void encode(ByteArrayOutputStream baos) throws Exception {
        this.addressIndicator.encode(baos);

        if (this.signallingPointCode != null) {
            this.signallingPointCode.encode(baos);
        }
        if (this.subSystemNumber != null) {
            baos.write(subSystemNumber.value() & 0xFF);
        }
        if (this.globalTitle != null) {
            globalTitle.encode(baos);
        }
    }

    public byte[] encode() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
        this.encode(baos);
        return baos.toByteArray();
    }

    @Override
    public void decode(ByteArrayInputStream bais) throws Exception {
        this.addressIndicator = new AddressIndicator();
        this.addressIndicator.decode(bais);
        if (this.addressIndicator.getPointCodeIndicator()) {
            this.signallingPointCode = new SignallingPointCode();
            this.signallingPointCode.decode(bais);
        }
        if (this.addressIndicator.getSSNIndicator()) {
            this.subSystemNumber = SubSystemNumber.getInstance(bais.read() & 0xff);
        }

        switch (this.addressIndicator.getGlobalTitleIndicator()) {
            case NATURE_OF_ADDRESS_IND_ONLY:
                this.globalTitle = new GT0001();
                this.globalTitle.decode(bais);
                break;
            case TRANSLATION_TYPE_ONLY:
                this.globalTitle = new GT0010();
                this.globalTitle.decode(bais);
                break;
            case TRANSLATION_TYPE_NP_ENC:
                this.globalTitle = new GT0011();
                this.globalTitle.decode(bais);
                break;
            case TRANSLATION_TYPE_NP_ENC_NATURE_OF_ADDRESS_IND:
                this.globalTitle = new GT0100();
                this.globalTitle.decode(bais);
                break;
        }
    }

    /**
     * @return the signallingPointCode
     */
    public SignallingPointCode getSignallingPointCode() {
        return signallingPointCode;
    }

    /**
     * @return the subSystemNumber
     */
    public SubSystemNumber getSubSystemNumber() {
        return subSystemNumber;
    }

    /**
     * @return the globalTitle
     */
    public GlobalTitle getGlobalTitle() {
        return globalTitle;
    }

    /**
     * @return the addressIndicator
     */
    public AddressIndicator getAddressIndicator() {
        return addressIndicator;
    }

    /**
     * @return the translated
     */
    public boolean isTranslated() {
        return translated;
    }

    /**
     * @param translated the translated to set
     */
    public void setTranslated(boolean translated) {
        this.translated = translated;
    }

    /**
     * @param signallingPointCode the signallingPointCode to set
     */
    public void setSignallingPointCode(SignallingPointCode signallingPointCode) {
        this.signallingPointCode = signallingPointCode;
        this.addressIndicator.setPointCodeIndicator(signallingPointCode != null);
    }

    /**
     * @param subSystemNumber the subSystemNumber to set
     */
    public void setSubSystemNumber(SubSystemNumber subSystemNumber) {
        this.subSystemNumber = subSystemNumber;
        this.addressIndicator.setSsnIndicator(subSystemNumber != null);
    }

    /**
     * @param globalTitle the globalTitle to set
     */
    public void setGlobalTitle(GlobalTitle globalTitle) {
        this.globalTitle = globalTitle;
        if (globalTitle != null) {
            this.addressIndicator.setGlobalTitleIndicator(globalTitle.getGlobalTitleIndicator());
        } else {
            this.addressIndicator.setGlobalTitleIndicator(GlobalTitleIndicator.NO_GLOBAL_TITLE_INCLUDED);
        }
    }

    /**
     * @return the mtpSap
     */
    public MTPServiceAccessPoint getMtpSap() {
        return mtpSap;
    }

    /**
     * @param mtpSap the mtpSap to set
     */
    public void setMtpSap(MTPServiceAccessPoint mtpSap) {
        this.mtpSap = mtpSap;
    }
}
