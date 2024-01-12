/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.reporting;

import azrc.az.sigtran.map.MAPListener;
import azrc.az.sigtran.map.MAPUserError;
import azrc.az.sigtran.map.ProviderError;
import azrc.az.sigtran.map.parameters.MAPApplicationContextName;

/**
 *
 * @author eatakishiyev
 */
public abstract class MAPMMEventReportingContextListener extends MAPListener {




    @Override
    public MAPApplicationContextName[] getMAPApplicationContexts() {
        return new MAPApplicationContextName[]{MAPApplicationContextName.MM_EVENT_REPORTING_CONTEXT};
    }

    public  void onMAPNoteMMEventIndication(Short invokeId, NoteMMEventArg request,
                                                    MAPMMEventReportingDialogue dialogue){}

    public  void onMAPNoteMMEventConfirmation(Short invokeId, NoteMMEventRes response,
                                              MAPMMEventReportingDialogue dialogue, MAPUserError userError, ProviderError providerError){}
}
