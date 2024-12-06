/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azrc.az.sigtran.tcap;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Thread-safe implementation of TCAP Transaction ID Pool
 *
 * @author eatakishiyev
 */
public class TransactionIdPool {

    private final Logger logger = LoggerFactory.getLogger(TransactionIdPool.class);
    private final Queue<Long> transactionIdPool = new ConcurrentLinkedQueue<>();
    //max count of transaction ids
    private final long min;
    private final long max;
    private final Object dummyObj = new Object();

    public TransactionIdPool(long min, long max) {
        logger.info(String.format("[TCAP]:Starting TransactionIdPool. MinValue => %d MaxValue => %d", min, max));
        this.min = min;
        this.max = max;
        this.initialize();
        logger.info("[TCAP]:TransactionIdPool started.");
    }

    private void initialize() {
        List<Long> tmp = new ArrayList<>();
        for (long i = min; i <= max; i++) {
            tmp.add(i);
        }

        Collections.shuffle(tmp);
        for (Long l : tmp) {
            transactionIdPool.add(l);
        }
    }

    public Long getId() throws ResourceLimitationException {

//        mutex.lock();
        try {
            Long transactionId = transactionIdPool.poll();

            if (transactionId == null) {
                throw new ResourceLimitationException();
            }

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("[TCAP]:Got transaction id %d", transactionId));
            }

            return transactionId;
        } catch (Exception ex) {
            logger.error("[TCAP]: Something gone wrong during getting transaction Id", ex);
            throw new ResourceLimitationException();
        } finally {
//            mutex.unlock();
        }
    }

    public void releaseId(Long transactionId) {
//        mutex.lock();
        try {
            this.transactionIdPool.offer(transactionId);

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("[TCAP]:Transaction id released  %d", transactionId));
            }
        } finally {
//            mutex.unlock();
        }
    }
}
