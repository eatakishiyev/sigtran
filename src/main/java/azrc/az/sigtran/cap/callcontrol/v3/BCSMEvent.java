/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap.callcontrol.v3;

import azrc.az.sigtran.cap.callcontrol.general.EventTypeBCSM;
import azrc.az.sigtran.cap.callcontrol.general.MonitorMode;

/**
 * BCSMEvent{PARAMETERS-BOUND : bound} ::= SEQUENCE { eventTypeBCSM [0]
 * EventTypeBCSM, monitorMode [1] MonitorMode, legID [2] LegID OPTIONAL,
 * dpSpecificCriteria [30] DpSpecificCriteria {bound} OPTIONAL, automaticRearm
 * [50] NULL OPTIONAL, ... }
 *
 * @author eatakishiyev
 */
public class BCSMEvent extends azrc.az.sigtran.cap.callcontrol.v2.BCSMEvent {

    public BCSMEvent() {
        super();
    }

    public BCSMEvent(EventTypeBCSM eventType, MonitorMode monitorMode) {
        super(eventType, monitorMode);
    }

}
