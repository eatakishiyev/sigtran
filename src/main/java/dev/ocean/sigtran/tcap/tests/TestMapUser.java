/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.tests;

import org.apache.logging.log4j.*;
import dev.ocean.sigtran.map.MAPDialogue;
import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPProvider;
import dev.ocean.sigtran.map.services.common.MapClose;
import dev.ocean.sigtran.map.services.common.MapOpen;
import dev.ocean.sigtran.map.services.common.MapPAbort;
import dev.ocean.sigtran.map.services.common.MapUAbort;
import dev.ocean.sigtran.sccp.general.ErrorReason;

/**
 *
 * @author eatakishiyev
 */
public class TestMapUser implements MAPListener {

    private final Logger logger = LogManager.getLogger(TestMapUser.class);

    public TestMapUser() {
        logger.info("Starting TestMapUser logic...");
    }

    @Override
    public void onDialogueTimeOut(MAPDialogue mapDialogue) {

    }

    @Override
    public void setMAPProvider(MAPProvider mapProvider) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMAPOpenInd(MapOpen mapOpen, MAPDialogue mapDialogue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMAPOpenConfirm(MapOpen mapOpen, MAPDialogue mapDialogue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMapDialogueReleased(MAPDialogue mapDialogue, MAPDialogue.TerminationReason reason) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMAPDelimiterInd(MAPDialogue mapDialogue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMAPNoticeInd(ErrorReason errorReason, MAPDialogue mapDialogue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMAPProviderAbortInd(MapPAbort mapPAbort, MAPDialogue mapDialogue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMAPUserAbortInd(MapUAbort mapUAbort, MAPDialogue mapDialogue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onMAPCloseInd(MAPDialogue mapDialogue) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MAPProvider getMAPProvider() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
