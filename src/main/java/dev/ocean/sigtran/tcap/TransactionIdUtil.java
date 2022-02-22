/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap;

import java.io.IOException;
import dev.ocean.sigtran.tcap.messages.exceptions.IncorrectSyntaxException;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;

/**
 *
 * @author root
 */
public class TransactionIdUtil {

    public static final int ORIGINATION_TRANSACTION_ID_TAG = 0x08;
    public static final int ORIGINATION_TRANSACTION_ID_TAG_CLASS = Tag.CLASS_APPLICATION;
    public static final boolean IS_ORIGNATION_TRANSACTION_ID_PRIMITIVE = true;
    public static final int DESTINATION_TRANSACTION_ID_TAG = 0x09;
    public static final int DESTINATION_TRANSACTION_ID_TAG_CLASS = Tag.CLASS_APPLICATION;
    public static final boolean IS_DESTINATION_TRANSACTION_ID_PRIMITIVE = true;

    private TransactionIdUtil() {

    }

    public static byte[] encode(long transactionId) {
        byte[] btid = new byte[4];
        btid[0] = (byte) ((transactionId >> 24) & 0xFF);
        btid[1] = (byte) ((transactionId >> 16) & 0xFF);
        btid[2] = (byte) ((transactionId >> 8) & 0xFF);
        btid[3] = (byte) (transactionId & 0xFF);
        return btid;
    }

    private static long _decode(byte[] octet) throws IncorrectSyntaxException {
        if (octet == null) {
            throw new NullPointerException("Parameter can not be null");
        }
        if (octet.length > 4) {
            throw new IncorrectSyntaxException(String.format("Incorrect length of TransactionId field. %d>4", octet.length));
        }

        long tag = (long) octet[0] & 0xFF;
        for (int i = 1; i < octet.length; i++) {
            tag = (long) (tag << 8) | (long) (octet[i] & 0xFF);
        }
        return tag;
    }

    public static long decode(AsnInputStream ais) throws IncorrectSyntaxException {
        try {
            byte[] octet = ais.readOctetString();
            return _decode(octet);
        } catch (IOException | AsnException ex) {
            throw new IncorrectSyntaxException(ex.getMessage());
        }
    }

    public static long decode(byte[] data) throws IncorrectSyntaxException {
        return _decode(data);
    }
}
