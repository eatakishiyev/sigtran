/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.cap;

import dev.ocean.sigtran.cap.api.CAPMessage;
import dev.ocean.sigtran.map.parameters.MAPApplicationContextVersion;
import java.util.Map;

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
