/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.sccp.controls;

import dev.ocean.sigtran.sccp.general.SCCPStackImpl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author eatakishiyev
 */
public class Reassembler implements Runnable {

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream(272);
    protected Future<?> task;
    private final AtomicInteger remainingSegmentsExpected = new AtomicInteger(0);
    private final String segmentationReference;
    private final SCCPConnectionlessControlImpl sccpConnectionlessControl;

    protected Reassembler(String segmentationReference, SCCPConnectionlessControlImpl sccpConnectionlessControl) {
        this.segmentationReference = segmentationReference;
        this.sccpConnectionlessControl = sccpConnectionlessControl;
        this.startTimer();
    }

    private void startTimer() {
        task = this.sccpConnectionlessControl.reassamblerTimer.schedule(this,
                SCCPStackImpl.getInstance().getReassemblyTimer(),
                TimeUnit.SECONDS);
    }

    protected void cancelTimer() {
        if (task != null
                && !task.isCancelled()) {
            this.task.cancel(true);
            this.task = null;
        }
    }

    @Override
    public void run() {
        this.sccpConnectionlessControl.segments.remove(segmentationReference);
        SCCPStackImpl.getInstance().reassambleTimerExpired.increment();
        this.cancelTimer();
    }

    public void decrementRSE() {
        remainingSegmentsExpected.decrementAndGet();
    }

    public int getRSE() {
        return remainingSegmentsExpected.get();
    }

    public void write(byte[] data) throws IOException {
        baos.write(data);
    }

    public byte[] read() {
        return baos.toByteArray();
    }
}
