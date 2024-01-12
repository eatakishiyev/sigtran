import azrc.az.sigtran.map.parameters.CGI;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.mobicents.protocols.asn.AsnException;

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
