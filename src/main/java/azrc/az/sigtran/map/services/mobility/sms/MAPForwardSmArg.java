/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.mobility.sms;

import azrc.az.sigtran.map.services.common.MAPArgument;
import azrc.az.sigtran.map.parameters.ExtensionContainer;
import azrc.az.sigtran.map.parameters.IMSI;
import azrc.az.sigtran.map.parameters.SMDeliveryTimerValue;
import azrc.az.sigtran.map.parameters.SM_RP_DA;
import azrc.az.sigtran.map.parameters.SM_RP_OA;
import azrc.az.sigtran.map.parameters.SignalInfo;
import azrc.az.sigtran.map.parameters.depricated.Time;

/**
 *
 * @author eatakishiyev
 */
public interface MAPForwardSmArg extends MAPArgument {

    /**
     * @param moreMessageToSend the moreMessageToSend to set
     */
    public void setMoreMessageToSend(Boolean moreMessageToSend);

    /**
     * @return the sMDeliveryTimer
     */
    public SMDeliveryTimerValue getsMDeliveryTimer();

    /**
     * @param sMDeliveryTimer the sMDeliveryTimer to set
     */
    public void setsMDeliveryTimer(SMDeliveryTimerValue sMDeliveryTimer);

    /**
     * @return the smDeliveryStartTime
     */
    public Time getSmDeliveryStartTime();

    /**
     * @param smDeliveryStartTime the smDeliveryStartTime to set
     */
    public void setSmDeliveryStartTime(Time smDeliveryStartTime);

    /**
     * @return the smRPDA
     */
    public SM_RP_DA getSmRPDA();

    /**
     * @param smRPDA the smRPDA to set
     */
    public void setSmRPDA(SM_RP_DA smRPDA);

    /**
     * @return the smRPOA
     */
    public SM_RP_OA getSmRPOA();

    /**
     * @param smRPOA the smRPOA to set
     */
    public void setSmRPOA(SM_RP_OA smRPOA);

    /**
     * @return the smRPUI
     */
    public SignalInfo getSmRPUI();

    /**
     * @param smRPUI the smRPUI to set
     */
    public void setSmRPUI(SignalInfo smRPUI);

    /**
     * @return the extensionContainer
     */
    public ExtensionContainer getExtensionContainer();

    /**
     * @param extensionContainer the extensionContainer to set
     */
    public void setExtensionContainer(ExtensionContainer extensionContainer);

    /**
     * @return the moreMessageToSend
     */
    public boolean isMoreMessageToSend();

    /**
     * @param moreMessageToSend the moreMessageToSend to set
     */
    public void setMoreMessageToSend(boolean moreMessageToSend);

    /**
     * @return the imsi
     */
    public IMSI getImsi();

    /**
     * @param imsi the imsi to set
     */
    public void setImsi(IMSI imsi);
}
