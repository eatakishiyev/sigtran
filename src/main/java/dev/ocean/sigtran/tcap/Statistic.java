/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dev.ocean.sigtran.tcap;

import dev.ocean.sigtran.sccp.address.SubSystemNumber;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author eatakishiyev
 */
public class Statistic {

    private final ScheduledExecutorService statisticScheduler;

    protected volatile AtomicInteger concurrentDialogueCount = new AtomicInteger();
    protected volatile AtomicInteger receivedBeginCount = new AtomicInteger();
    protected volatile AtomicInteger receivedContinueCount = new AtomicInteger();
    protected volatile AtomicInteger receivedEndCount = new AtomicInteger();
    protected volatile AtomicInteger receivedAbortCount = new AtomicInteger();
    protected volatile AtomicInteger receivedNoticeCount = new AtomicInteger();
    protected volatile AtomicInteger receivedUnparsableMessage = new AtomicInteger();
    protected volatile AtomicInteger receivedUnknownMessage = new AtomicInteger();

    protected volatile AtomicInteger sentBeginCount = new AtomicInteger();
    protected volatile AtomicInteger sentContinueCount = new AtomicInteger();
    protected volatile AtomicInteger sentEndCount = new AtomicInteger();
    protected volatile AtomicInteger sentAbortCount = new AtomicInteger();
    protected volatile AtomicInteger sentNoticeCount = new AtomicInteger();

    protected volatile AtomicInteger receivedInvokeCount = new AtomicInteger();
    protected volatile AtomicInteger receivedReturnResultNotLastCount = new AtomicInteger();
    protected volatile AtomicInteger receivedReturnResultLastCount = new AtomicInteger();
    protected volatile AtomicInteger receivedReturnErrorCount = new AtomicInteger();
    protected volatile AtomicInteger receivedRejectCount = new AtomicInteger();
    protected volatile AtomicInteger receivedMalformedOperationCount = new AtomicInteger();

    protected volatile AtomicInteger timeoutedDialogueCount = new AtomicInteger();
    protected volatile AtomicInteger timeoutedOperationCount = new AtomicInteger();
    protected volatile AtomicInteger createdDialogueCount = new AtomicInteger();

    private final String STAT_FOLDER = "./stat/";
    private final TCAPStack tcapStack;

    public Statistic(String name, TCAPStack tcapStack) throws FileNotFoundException, IOException {
        Path pathToStat = Paths.get(STAT_FOLDER);
        String STAT_FILE = STAT_FOLDER.concat("tcap").concat("-").concat(name);
        if (!Files.exists(pathToStat)) {
            Files.createDirectory(pathToStat);
        }
        this.tcapStack = tcapStack;
        MappedByteBuffer statFile = new RandomAccessFile(STAT_FILE, "rw")
                .getChannel().map(FileChannel.MapMode.READ_WRITE, 0, 4098);
        statisticScheduler
                = Executors.newSingleThreadScheduledExecutor((Runnable r)
                        -> new Thread(r, "TCAPStatisticsScheduler-" + name));
        statisticScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                StringBuilder sb = new StringBuilder();

                sb.append("ConcurrentDialogueCount = ").
                        append(tcapStack.dialoguesCount()).append("\n");
                sb.append("ReceivedBeginCount = ").
                        append(receivedBeginCount.getAndAdd(0)).append("\n");
                sb.append("ReceivedContinueCount = ").
                        append(receivedContinueCount.getAndAdd(0)).append("\n");
                sb.append("ReceivedEndCount = ").
                        append(receivedEndCount.getAndAdd(0)).append("\n");
                sb.append("ReceivedAbortCount = ").
                        append(receivedAbortCount.getAndAdd(0)).append("\n");
                sb.append("ReceivedUnparsableMessage = ").
                        append(receivedUnparsableMessage.getAndAdd(0)).append("\n");
                sb.append("ReceivedUnknownMessage = ").
                        append(receivedUnknownMessage.getAndAdd(0)).append("\n");
                sb.append("ReceivedNoticeCount = ").
                        append(receivedNoticeCount.getAndAdd(0)).append("\n");
                sb.append("SentBeginCount = ").
                        append(sentBeginCount.getAndAdd(0)).append("\n");
                sb.append("SentContinueCount = ").
                        append(sentContinueCount.getAndAdd(0)).append("\n");
                sb.append("SentEndCount = ").
                        append(sentEndCount.getAndAdd(0)).append("\n");
                sb.append("SentAbortCount = ").
                        append(sentAbortCount.getAndAdd(0)).append("\n");
                sb.append("SentNoticeCount = ").
                        append(sentNoticeCount.getAndAdd(0)).append("\n");
                sb.append("ReceivedInvokeCount = ").
                        append(receivedInvokeCount.getAndAdd(0)).append("\n");
                sb.append("ReceivedReturnResulNotLastCount = ").
                        append(receivedReturnResultNotLastCount.getAndAdd(0)).append("\n");
                sb.append("ReceivedReturnResultLastCount = ").
                        append(receivedReturnResultLastCount.getAndAdd(0)).append("\n");
                sb.append("ReceivedReturnErrorCode = ").
                        append(receivedReturnErrorCount.getAndAdd(0)).append("\n");
                sb.append("ReceivedRejectCount = ").
                        append(receivedRejectCount.getAndAdd(0)).append("\n");
                sb.append("ReceivedMalformedOperationCount = ").
                        append(receivedMalformedOperationCount.get()).append("\n");
                sb.append("TimeOutedDialogues = ").
                        append(timeoutedDialogueCount.getAndAdd(0)).append("\n");
                sb.append("CreatedDialogues = ").
                        append(createdDialogueCount.getAndAdd(0)).append("\n");
                statFile.clear();
                sb.append("TimeOutedOperations = ").
                        append(timeoutedOperationCount.get()).append("\n");
                statFile.put(sb.toString().getBytes());
            }
        }, 0, 5, TimeUnit.SECONDS);
    }

}
