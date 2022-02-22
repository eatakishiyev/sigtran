/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.map.service.mm.event.reporting;

import dev.ocean.sigtran.map.MAPListener;
import dev.ocean.sigtran.map.MAPUserError;
import dev.ocean.sigtran.map.ProviderError;

/**
 *
 * @author eatakishiyev
 */
public interface MAPMMEventReportingContextListener extends MAPListener {

    public void onMAPNoteMMEventIndication(Short invokeId, NoteMMEventArg request,
            MAPMMEventReportingDialogue dialogue);

    public void onMAPNoteMMEventConfirmation(Short invokeId, NoteMMEventRes response,
            MAPMMEventReportingDialogue dialogue, MAPUserError userError, ProviderError providerError);
}
