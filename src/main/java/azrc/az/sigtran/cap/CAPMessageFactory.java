/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.cap;

import azrc.az.sigtran.cap.api.CAPMessage;

/**
 *
 * @author eatakishiyev
 */
public class CAPMessageFactory {

    
    
    public static CAPMessage createCAPMessage(byte[] data, OperationCodes opCode) {
        CAPMessage message = null;
        switch (opCode) {
            case CONNECT:

                break;
        }

        return message;
    }
}
