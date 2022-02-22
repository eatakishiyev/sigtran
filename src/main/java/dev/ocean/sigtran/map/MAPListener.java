/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map;

import dev.ocean.sigtran.map.services.common.MapClose;
import dev.ocean.sigtran.map.services.common.MapOpen;
import dev.ocean.sigtran.map.services.common.MapPAbort;
import dev.ocean.sigtran.map.services.common.MapUAbort;
import dev.ocean.sigtran.sccp.general.ErrorReason;

/**
 * @author eatakishiyev
 */
public interface MAPListener {

    public void setMAPProvider(MAPProvider mapProvider);

    public void onMAPOpenInd(MapOpen mapOpen, MAPDialogue mapDialogue);

    public void onMAPOpenConfirm(MapOpen mapOpen, MAPDialogue mapDialogue);

    public void onMAPCloseInd(MAPDialogue mapDialogue);

    public void onMAPDelimiterInd(MAPDialogue mapDialogue);

    public void onMAPNoticeInd(ErrorReason errorReason, MAPDialogue mapDialogue);

    public void onMAPProviderAbortInd(MapPAbort mapPAbort, MAPDialogue mapDialogue);

    public void onMAPUserAbortInd(MapUAbort mapUAbort, MAPDialogue mapDialogue);

    public void onDialogueTimeOut(MAPDialogue mapDialogue);

    public void onMapDialogueReleased(MAPDialogue mapDialogue, MAPDialogue.TerminationReason reason);

    public MAPProvider getMAPProvider();


}
