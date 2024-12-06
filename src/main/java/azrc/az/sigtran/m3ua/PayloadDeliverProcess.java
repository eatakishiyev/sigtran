/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.m3ua;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author eatakishiyev
 */
public class PayloadDeliverProcess implements Runnable {

    private final MTPTransferMessage message;
    private final M3UAUser user;
    private final Logger logger = LoggerFactory.getLogger(PayloadDeliverProcess.class);

    public PayloadDeliverProcess(MTPTransferMessage message, M3UAUser user) {
        this.message = message;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Fire mtpTransferIndication: %s", message));
            }
            user.onMtpTransferIndication(message);
        } catch (Exception ex) {
            logger.error("Error on deliver message to User", ex);
        }
    }
}
