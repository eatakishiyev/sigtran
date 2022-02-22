/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap.tests;

import dev.ocean.sigtran.Sigtran;
import dev.ocean.sigtran.cap.callcontrol.general.ApplyChargingReportArg;
import dev.ocean.sigtran.map.*;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextVersion;
import dev.ocean.sigtran.map.service.anytime.information.enquiry.AnyTimeInterrogationArg;
import dev.ocean.sigtran.map.service.anytime.information.enquiry.AnyTimeInterrogationRes;
import dev.ocean.sigtran.map.service.anytime.information.enquiry.MAPAnyTimeEnquiryContextListener;
import dev.ocean.sigtran.map.service.anytime.information.enquiry.MAPAnyTimeInterrogationDialogue;
import dev.ocean.sigtran.map.services.common.MapOpen;
import dev.ocean.sigtran.map.services.common.MapPAbort;
import dev.ocean.sigtran.map.services.common.MapUAbort;
import dev.ocean.sigtran.map.services.equipment.management.MAPEquipmentManagementDialogue;
import dev.ocean.sigtran.sccp.address.NatureOfAddress;
import dev.ocean.sigtran.sccp.address.RoutingIndicator;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.address.SCCPAddressFactory;
import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import dev.ocean.sigtran.sccp.general.ErrorReason;
import dev.ocean.sigtran.sccp.general.NumberingPlan;
import dev.ocean.sigtran.sccp.globaltitle.GT0100;
import dev.ocean.sigtran.sccp.messages.connectionless.SCCPConnectionlessMessageFactory;
import dev.ocean.sigtran.sccp.messages.connectionless.XUnitDataService;
import dev.ocean.sigtran.utils.ByteUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.mobicents.protocols.asn.AsnInputStream;

/**
 * @author eatakishiyev
 */
public class testJndi {

    public static void main(String... args) throws Exception {
        MAPListener mapDialogue = new TestMapUser();

        if (mapDialogue instanceof TestMapUser) {
            System.out.println("This listener is MAPAnyTimeEnquiryContextListener type");
        }
    }
}
