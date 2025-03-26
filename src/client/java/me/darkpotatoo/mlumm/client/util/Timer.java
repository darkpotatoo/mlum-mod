package me.darkpotatoo.mlumm.client.util;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timer {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private long startTime;
    private static final Logger LOGGER = LogUtils.getLogger();
    private static long milliseconds = 0;

    public Timer(double seconds) {
        milliseconds = (long) (seconds*1000);
        this.startTime = System.currentTimeMillis();
        scheduler.scheduleAtFixedRate(this::getPassedFiveMinutes, 0, milliseconds, TimeUnit.MILLISECONDS);
        LOGGER.debug("Started a timer for " + seconds + " seconds");
    }

    private void getPassedFiveMinutes() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        if (elapsedTime >= milliseconds * 1000) {
            startTime = currentTime;
        }
    }

}