/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.sccp.messages.connectionless;

import azrc.az.sigtran.sccp.address.SCCPAddress;
import azrc.az.sigtran.sccp.general.ErrorReason;
import azrc.az.sigtran.sccp.general.SCCPStackImpl;
import azrc.az.sigtran.sccp.messages.MessageType;
import azrc.az.sigtran.utils.ByteUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.Serializable;

/**
 *
 * @author eatakishiyev
 */
public class SCCPConnectionlessMessageFactory implements Serializable {

    private final static Logger logger = LoggerFactory.getLogger(SCCPConnectionlessMessageFactory.class);

    private SCCPConnectionlessMessageFactory() {
    }

    public static UnitData createUnitData() {
        UnitData unitData = new UnitData();
        return unitData;
    }

    public static SCCPMessage createUnitData(ByteArrayInputStream bais) throws Exception {
        return new UnitData(bais);

    }

    public static UnitData createUnitData(SCCPAddress calledPartyAddress, SCCPAddress callingPartyAddress, byte[] userData) {
        UnitData unitData = new UnitData(calledPartyAddress, callingPartyAddress, userData);
        return unitData;
    }

    public static UnitDataService createUnitDataService() {
        UnitDataService unitDataService = new UnitDataService();
        return unitDataService;
    }

    public static UnitDataService createUnitDataService(ByteArrayInputStream bais) throws Exception {
        return new UnitDataService(bais);
    }

    public static UnitDataService createUnitDataService(ErrorReason errorReason, SCCPAddress calledPartyAddress, SCCPAddress callingPartyAddress, byte[] userData) {
        UnitDataService unitDataService = new UnitDataService(errorReason, calledPartyAddress, callingPartyAddress, userData);
        return unitDataService;
    }

    public static XUnitData createExtendedUnitData(SCCPAddress calledPartyAddress,
            SCCPAddress callingPartyAddress, int hopCounter, byte[] userData) {
        XUnitData xUnitData = new XUnitData(calledPartyAddress, callingPartyAddress,
                hopCounter, userData);
        return xUnitData;
    }

    public static XUnitData createExtendedUnitData() {
        XUnitData xUnitData = new XUnitData();
        return xUnitData;
    }

    public static XUnitData createExtendedUnitData(ByteArrayInputStream bais) throws Exception {
        return new XUnitData(bais);
    }

    public static XUnitDataService createExtendedUnitDataService() {
        return new XUnitDataService();
    }

    public static XUnitDataService createExtendedUnitDataService(ErrorReason returnCause,
            int hopCounter, SCCPAddress calledPartyAddress, SCCPAddress callingPartyAddress, byte[] data) {
        return new XUnitDataService(returnCause, hopCounter, calledPartyAddress,
                callingPartyAddress, data);
    }

    public static XUnitDataService createExtendedUnitDataService(ByteArrayInputStream bais) throws Exception {
        return new XUnitDataService(bais);
    }

    public static SCCPMessage createMessage(byte[] data) {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        SCCPMessage sccpMessage = null;
        try {

            MessageType messageType = MessageType.getInstance(bais.read());
            switch (messageType) {
                case UDT:
                    sccpMessage = createUnitData(bais);

                    break;
                case UDTS:
                    sccpMessage = createUnitDataService(bais);

                    break;
                case XUDT:
                    sccpMessage = createExtendedUnitData(bais);

                    break;
                case XUDTS:
                    sccpMessage = createExtendedUnitDataService(bais);

                    break;
            }
        } catch (Throwable th) {
            logger.error("Rx: Can not create SCCP message from incoming bytes: " + ByteUtils.bytes2Hex(data), th);
            return null;
        }
        return sccpMessage;
    }

}
