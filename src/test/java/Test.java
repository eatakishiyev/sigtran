import azrc.az.sigtran.common.exceptions.IncorrectSyntaxException;
import azrc.az.sigtran.map.parameters.CGI;
import azrc.az.sigtran.map.services.errors.CallBarred;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static CGI cgi = new CGI();


    @org.junit.jupiter.api.Test
    public void testMAPCallBarredError() throws IncorrectSyntaxException {
        byte[] bytes = DatatypeConverter.parseHexBinary("30030a0101");
        CallBarred callBarred = new CallBarred();
        callBarred.decode(new AsnInputStream(bytes));
    }

}
