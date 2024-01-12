/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.dialoguePDU;

import java.io.IOException;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnInputStream;

/**
 *
 * @author eatakishiyev
 */
public final class MAPDialoguePDUFactory {

    private MAPDialoguePDUFactory() {

    }

    public static MAPProviderAbortInfo createMAPPRoviderAbortInfo() {
        return new MAPProviderAbortInfo();
    }

    public static MAPProviderAbortInfo createMAPProviderAbortInf(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        MAPProviderAbortInfo mapProviderAbortInfo = new MAPProviderAbortInfo();
        mapProviderAbortInfo.decode(ais);
        return mapProviderAbortInfo;
    }

    public static MAPDialoguePDU createMAPDialoguePDU(AsnInputStream ais) throws IncorrectSyntaxException, UnexpectedDataException {
        int tag = 0;
        try {
            tag = ais.readTag();
        } catch (IOException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
        switch (tag) {
            case 0x00://MAPOpenInfo
                MAPOpenInfo mapOpenInfo = new MAPOpenInfo();
                mapOpenInfo.decode(ais);
                return mapOpenInfo;
            case 0x01://MAPAcceptInfo
                MAPAcceptInfo mapAcceptInfo = new MAPAcceptInfo();
                mapAcceptInfo.decode(ais);
                return mapAcceptInfo;
            case 0x02://MAPCloseInfo
                MAPCloseInfo mapCloseInfo = new MAPCloseInfo();
                mapCloseInfo.decode(ais);
                return mapCloseInfo;
            case 0x03://MAPRefuseInfo
                MAPRefuseInfo mapRefuseInfo = new MAPRefuseInfo();
                mapRefuseInfo.decode(ais);
                return mapRefuseInfo;
            case 0x04://MAPUserAbortInfo
                MAPUserAbortInfo mapUserAbortInfo = new MAPUserAbortInfo();
                mapUserAbortInfo.decode(ais);
                return mapUserAbortInfo;
            case 0x05://MAPProviderAbortInfo
                MAPProviderAbortInfo mapProviderAbortInfo = new MAPProviderAbortInfo();
                mapProviderAbortInfo.decode(ais);
                return mapProviderAbortInfo;
        }
        return null;
    }
}
