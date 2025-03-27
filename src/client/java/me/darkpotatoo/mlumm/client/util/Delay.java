package me.darkpotatoo.mlumm.client.util;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Delay {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private long startTime;
    private final Logger LOGGER = LogUtils.getLogger();
    private final long seconds;

    public Delay(long seconds, Runnable method) {
        this.seconds = seconds;
        this.startTime = System.currentTimeMillis();
        scheduler.scheduleAtFixedRate(method, 0, seconds, TimeUnit.SECONDS);
        LOGGER.debug("Started a timer for " + seconds + "s");
    }
}