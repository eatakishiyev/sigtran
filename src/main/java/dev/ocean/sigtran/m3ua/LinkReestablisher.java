/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.m3ua;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.logging.log4j.*;

/**
 *
 * @author eatakishiyev
 */
public class LinkReestablisher {

    private final Logger logger = LogManager.getLogger(LinkReestablisher.class);
    private Asp link;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public void scheduleTask(Asp link) {
        synchronized (link) {
            this.link = link;
            executor.submit(new LinkReestablisherTask());
        }
    }

    private class LinkReestablisherTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                link.start();
            } catch (Exception ex) {
                logger.error("error: ", ex);
            }
        }

    }
}
