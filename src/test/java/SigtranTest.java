import azrc.az.sctp.SctpAssociation;
import azrc.az.sigtran.Sigtran;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.m3ua.AspImpl;
import azrc.az.sigtran.m3ua.M3UAStack;
import azrc.az.sigtran.m3ua.M3UAStackImpl;
import azrc.az.sigtran.m3ua.configuration.M3UAConfiguration;
import azrc.az.sigtran.map.MAPStack;
import azrc.az.sigtran.map.MAPStackImpl;
import azrc.az.sigtran.map.parameters.CGI;
import azrc.az.sigtran.map.services.errors.CallBarred;
import azrc.az.sigtran.map.services.mobility.sms.MAPForwardSmArg;
import azrc.az.sigtran.map.services.mobility.sms.MAPShortMessageRelayContextListener;
import azrc.az.sigtran.map.services.mobility.sms.MAPShortMessageRelayDialogue;
import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.address.SubSystemNumber;
import azrc.az.sigtran.sccp.general.SCCPStack;
import azrc.az.sigtran.sccp.general.SCCPStackImpl;
import azrc.az.sigtran.sccp.general.configuration.SCCPConfiguration;
import azrc.az.sigtran.sccp.messages.MessageHandling;
import azrc.az.sigtran.tcap.TCAPDialogue;
import azrc.az.sigtran.tcap.TCAPStack;
import azrc.az.sigtran.tcap.messages.AbortMessageImpl;
import azrc.az.sigtran.tcap.messages.MessageFactory;
import azrc.az.sigtran.tcap.parameters.ApplicationContextImpl;
import azrc.az.sms.smstpdu.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.sun.nio.sctp.Association;
import com.sun.nio.sctp.MessageInfo;
import org.apache.log4j.PropertyConfigurator;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.Executors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author eatakishiyev
 */
@ExtendWith(MockitoExtension.class)
public class SigtranTest {

    private static CGI cgi = new CGI();

    @Test
    public void testTcapAbortParsing() throws azrc.az.sigtran.tcap.messages.exceptions.IncorrectSyntaxException {
        byte[] data = DatatypeConverter.parseHexBinary("673249045d01554e6b2a2828060700118605010101a01d611b80020780a109060704000001001902a203020101a305a103020102");
        AbortMessageImpl message = (AbortMessageImpl) MessageFactory.createMessage(new AsnInputStream(data));
        TCAPDialogue tcapDialogue = new TCAPDialogue(null, 1l, false, null);
        tcapDialogue.setApplicationContext(new ApplicationContextImpl());
        tcapDialogue.setState(TCAPDialogue.TCAPDialogueState.INITIATION_SENT);
        tcapDialogue.abortReceived(message, null, null);
        System.out.println(message);

    }

    @Test
    public void testMAPMoForwardSmV2() throws Exception {
        String data = "6281da4804e8b607006b1a2818060700118605010101a00d600ba1090607040000010015026c81b5a181b202013102012e3081a984079199547570707082079199549552983004819401220481694500f68b000000011505c2f3de000069b4875792673d092568336d01a85a375ad6b448e6cc233d3d7fb5d17f4560f7881da0c34ae0ffbae7e9acd0388e53b29194fa49b23ecc1914118855c39eb4c1c3a5877b4c2a3a0c6ece802c648a942e224299df4eadd3a3471811ad3582168b3ac3b2b93750294a5864a98be7038ce5fe7bc5a855b9d23c4271c722bdf84b36";
        TCAPStack tcapStack = new TCAPStack("testTcap", 0, 1000, 100);
        MAPStackImpl mapStack = new MAPStackImpl(tcapStack.getProvider(), 1000, 1000);
        mapStack.addMapUser(new MAPShortMessageRelayContextListener() {
            @Override
            public void onMAPForwardSmIndication(Short invokeId, MAPForwardSmArg arg, MAPShortMessageRelayDialogue mapDialogue) {
                if (arg.getImsi() == null) {
                    Assertions.assertNull(arg.getImsi());
                }
                try {
                    SmsSubmitTpduImpl smsSubmitTPdu = new SmsSubmitTpduImpl(arg.getSmRPUI().getData(), null);

                    System.out.println(smsSubmitTPdu.getReplyPathExists());
                    System.out.println(smsSubmitTPdu.getStatusReportRequest());
                    System.out.println(smsSubmitTPdu.getProtocolIdentifier().getCode());
                    System.out.println(smsSubmitTPdu.getUserData().getEncodedData());
                    System.out.println(smsSubmitTPdu.getMessageReference());
                    System.out.println(smsSubmitTPdu.getUserDataLength());
                    DataCodingScheme dataCodingScheme = smsSubmitTPdu.getDataCodingScheme();
                    if (dataCodingScheme.getCharacterSet() == CharacterSet.GSM8) {
                        try {
                            long l = calculateValidityPeriod(System.currentTimeMillis(),
                                    smsSubmitTPdu.getValidityPeriod(), smsSubmitTPdu.getValidityPeriodFormat());
                            System.out.println(l);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        smsSubmitTPdu.getUserData().decode();

                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });
        tcapStack.getProvider().setUser(mapStack);
//

        tcapStack.onMessage(new SCCPAddress(), new SCCPAddress(), DatatypeConverter.parseHexBinary(data),
                true, 1, MessageHandling.RETURN_MESSAGE_ON_ERROR);


    }

    @Test
    public void testM3UAPayloadDecode() throws Exception {

        PropertyConfigurator.configure("/Users/ElnurA/IdeaProjects/sigtran/log4j.properties");
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        M3UAConfiguration m3UAConfiguration =
                objectMapper.readValue(new File("/Users/ElnurA/Downloads/sigtran-m3ua.yaml"), M3UAConfiguration.class);
        SCCPConfiguration sccpConfiguration =
                objectMapper.readValue(new File("/Users/ElnurA/Downloads/sigtran-sccp.yaml"), SCCPConfiguration.class);

        Sigtran sigtran = new Sigtran();
        M3UAStack m3UAStack = sigtran.createM3UAStack(m3UAConfiguration);
        m3UAStack.setWorkersCount(10);
        SCCPStack sccpStack = sigtran.createSCCPStack(sccpConfiguration);
        MAPStack mapStack = sigtran.createMAPStack("test-map", 0, 100000, 50,
                SubSystemNumber.MSC, 60, 10);
        mapStack.addMapUser(new MAPShortMessageRelayContextListener() {
            @Override
            public void onMAPForwardSmIndication(Short invokeId, MAPForwardSmArg arg, MAPShortMessageRelayDialogue mapDialogue) {
                if (arg.getImsi() == null) {
                    Assertions.assertNull(arg.getImsi());
                }
                try {
                    SmsSubmitTpduImpl smsSubmitTPdu = new SmsSubmitTpduImpl(arg.getSmRPUI().getData(), null);
                    if (arg.getImsi() != null) {
                        System.out.println(arg.getImsi());
                    }
                    System.out.println(arg.getImsi().getValue());
                    System.out.println(mapDialogue.getCallingAddress().getGlobalTitle().getGlobalTitleAddressInformation());
                    System.out.println(smsSubmitTPdu.getReplyPathExists());
                    System.out.println(smsSubmitTPdu.getStatusReportRequest());
                    System.out.println(smsSubmitTPdu.getProtocolIdentifier().getCode());
                    System.out.println(smsSubmitTPdu.getMessageReference());
                    System.out.println(smsSubmitTPdu.getUserDataLength());

                    System.out.println(arg.getSmRPOA().getMsisdn().getAddress());
                    System.out.println((byte) arg.getSmRPOA().getMsisdn().getAddressNature().value());
                    System.out.println((byte) arg.getSmRPOA().getMsisdn().getNumberingPlan().value());
                    System.out.println(arg.getSmRPDA().getServiceCenterAddressDA().getAddress());
                    System.out.println(mapDialogue.getCallingAddress().getGlobalTitle().getGlobalTitleAddressInformation());
                    System.out.println(mapDialogue.getCalledAddress().getGlobalTitle().getGlobalTitleAddressInformation());
                    long l = calculateValidityPeriod(System.currentTimeMillis(),
                            smsSubmitTPdu.getValidityPeriod(), smsSubmitTPdu.getValidityPeriodFormat());
                    System.out.println(l);

                    System.out.println("dialogId:");
                    DataCodingScheme dataCodingScheme = smsSubmitTPdu.getDataCodingScheme();
                    if (dataCodingScheme.getCharacterSet() == CharacterSet.GSM8) {
                        try {
                            System.out.println(smsSubmitTPdu.getUserData().getEncodedData());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        smsSubmitTPdu.getUserData().decode();

                    }
                } catch (Exception ee) {
                    ee.printStackTrace();
                }
            }
        });


        MessageInfo outgoing = MessageInfo.createOutgoing(new InetSocketAddress(2605), 3367);
        outgoing.payloadProtocolID(3);
        outgoing.streamNumber(2);
        byte[] data = DatatypeConverter.parseHexBinary("01000101000001140210010b000000340000015e030300090980030e190b12080012049954050030050b1208001204995405003024dd6281da4804e8b607006b1a2818060700118605010101a00d600ba1090607040000010015026c81b5a181b202013102012e3081a984079199547570707082079199549552983004819401220481694500f68b000000011505c2f3de000069b4875792673d092568336d01a85a375ad6b448e6cc233d3d7fb5d17f4560f7881da0c34ae0ffbae7e9acd0388e53b29194fa49b23ecc1914118855c39eb4c1c3a5877b4c2a3a0c6ece802c648a942e224299df4eadd3a3471811ad3582168b3ac3b2b93750294a5864a98be7038ce5fe7bc5a855b9d23c4271c722bdf84b3600");
        AspImpl asp = (AspImpl) ((M3UAStackImpl) m3UAStack).getM3uaManagement().getAsp("STP2BTC");
        SctpAssociation sctpAssociation = Mockito.mock(SctpAssociation.class);
        Association association = Mockito.mock(Association.class);
        Mockito.when(sctpAssociation.association()).thenReturn(association);
        Mockito.when(association.maxInboundStreams()).thenReturn(3);
        Mockito.when(association.maxOutboundStreams()).thenReturn(3);

        asp.onCommunicationUp(sctpAssociation);
        asp.onData(data, outgoing);
        System.in.read();
//        PayloadData message = (PayloadData) azrc.az.sigtran.m3ua.messages.MessageFactory.createMessage(data);
//        System.out.println(message);
//
//        SCCPMessage sccpMessage = SCCPConnectionlessMessageFactory.createMessage(message.getProtocolData().getUserProtocolData());
//        System.out.println(sccpMessage);
//
//        TCAPMessage tcapMessage = MessageFactory.createMessage(new AsnInputStream(sccpMessage.getData()));
//        DialoguePortionImpl dialoguePortion = DialogueFactory.createDialoguePortion(new AsnInputStream(tcapMessage.getDialogPortion()));
//        System.out.println(tcapMessage);
    }

    private Long calculateValidityPeriod(Long serviceCentreTime, ValidityPeriod validityPeriod, ValidityPeriodFormat validityPeriodFormat) throws ParseException {
        long vp = 0;
        switch (validityPeriodFormat) {
            case fieldPresentAbsoluteFormat:
                AbsoluteTimeStamp absoluteTimeStamp = validityPeriod.getAbsoluteFormatValue();
                vp = formatAbsolute(absoluteTimeStamp.getYear(),
                        absoluteTimeStamp.getMonth(),
                        absoluteTimeStamp.getDay(),
                        absoluteTimeStamp.getHour(),
                        absoluteTimeStamp.getMinute(),
                        absoluteTimeStamp.getSecond(),
                        absoluteTimeStamp.getTimeZone());
                break;
            case fieldPresentRelativeFormat:
                vp = formatRelative(validityPeriod.getRelativeFormatValue(), serviceCentreTime);
                break;
            default:
                vp = 0;
        }
        return vp;
    }

    public static long formatRelative(int tpVp, long serviceCentreTime) {
        long minutes = 0L;
        if (tpVp >= 0 && tpVp <= 143) {
            minutes = (long) ((tpVp + 1) * 5);
        } else if (tpVp >= 144 && tpVp <= 167) {
            minutes = (long) (720 + (tpVp - 143) * 30);
        } else if (tpVp >= 168 && tpVp <= 196) {
            minutes = (long) ((tpVp - 166) * 1440);
        } else if (tpVp >= 197 && tpVp <= 255) {
            minutes = (long) ((tpVp - 192) * 10080);
        }

        return serviceCentreTime + minutes * 60L * 1000L;
    }

    public static long formatAbsolute(int year, int month, int day, int hour, int minute, int seconds, int timeZone) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
        String date = String.format("%02d%02d%02d%02d%02d%02d", year, month, day, hour, minute, seconds);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        long currentOffsetInMillis = (long) calendar.get(15);
        long currentDaylighSavings = (long) calendar.get(16);
        long difference = currentOffsetInMillis + currentDaylighSavings - (long) timeZone * 15L * 60L * 1000L;
        long time = simpleDateFormat.parse(date).getTime() + difference;
        return time;
    }

    @Test
    public void testMAPCallBarredError() throws IncorrectSyntaxException {
        byte[] bytes = DatatypeConverter.parseHexBinary("30030a0101");
        CallBarred callBarred = new CallBarred();
        callBarred.decode(new AsnInputStream(bytes));
    }

}
