
import dev.ocean.sigtran.Sigtran;
import dev.ocean.sigtran.common.exceptions.UnexpectedDataException;
import dev.ocean.sigtran.map.MAPStackImpl;
import dev.ocean.sigtran.map.parameters.CGI;
import dev.ocean.sigtran.map.services.mobility.sms.SendRoutingInfoForSMRes;
import dev.ocean.sigtran.sccp.address.SCCPAddress;
import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import dev.ocean.sigtran.sccp.general.ErrorReason;
import dev.ocean.sigtran.sccp.general.SCCPProvider;
import dev.ocean.sigtran.sccp.general.SCCPUser;
import dev.ocean.sigtran.sccp.messages.MessageHandling;
import dev.ocean.sigtran.sccp.messages.MessageType;
import dev.ocean.sigtran.tcap.TCAPProvider;
import dev.ocean.sigtran.tcap.TCAPStack;
import dev.ocean.sigtran.tcap.TCUser;
import dev.ocean.sigtran.tcap.components.ComponentFactory;
import dev.ocean.sigtran.tcap.components.ReturnResultLast;
import dev.ocean.sigtran.tcap.dialogueAPDU.DialoguePortionImpl;
import dev.ocean.sigtran.tcap.messages.EndMessage;
import dev.ocean.sigtran.tcap.messages.MessageFactory;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import dev.ocean.sigtran.utils.ByteUtils;

import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;

import javax.xml.bind.DatatypeConverter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author eatakishiyev
 */
public class Test {
    static Logger logger = LogManager.getLogger(Test.class);

    private static CGI cgi = new CGI();

    public static void main(String[] args) throws AsnException {

        long start = 994702010000l;
        long end = 994702019999l;

        for(long l = start; l < end ;l++){
            logger.info("Test created");
            MDC.put("msisdn", l);
        }
        MDC.put("Count" , (end - start) + 1);
    }

}
