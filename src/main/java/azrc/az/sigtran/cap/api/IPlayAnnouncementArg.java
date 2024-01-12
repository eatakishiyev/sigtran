/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.api;

import azrc.az.sigtran.cap.parameters.InformationToSend;

/**
 *
 * @author eatakishiyev
 */
public interface IPlayAnnouncementArg extends CAPMessage{

    public InformationToSend getInformationToSend();

    public void setInformationToSend(InformationToSend informationToSend);

    public boolean isDisconnectFromIPForbidden();

    public void setDisconnectFromIPForbidden(boolean disconnectIPForbidden);

    public boolean isRequestAnnouncementComplete();

    public void setRequestAnnouncementComplete(boolean requestAnnouncementComplete);

    public byte[] getExtensions();

    public void setExtensions(byte[] extensions);
}
