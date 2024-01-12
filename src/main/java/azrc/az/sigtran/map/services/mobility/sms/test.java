/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.map.services.mobility.sms;

import java.io.IOException;
import java.util.HashMap;
import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.common.exceptions.UnexpectedDataException;
import org.mobicents.protocols.asn.AsnException;

/**
 *
 * @author eatakishiyev
 */
public class test {

    public static void main(String... args) throws IncorrectSyntaxException, UnexpectedDataException, IOException, AsnException {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("a", Integer.MIN_VALUE);
        map.put("b", Integer.MIN_VALUE);
        map.put("c", Integer.MIN_VALUE);
//        ListIterator iterator = (ListIterator) map.keySet().iterator();
        System.out.println("");
    }

}
