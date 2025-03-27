package me.darkpotatoo.mlumm.client.statistical;

import com.mojang.logging.LogUtils;
import me.darkpotatoo.mlumm.client.util.Delay;
import org.slf4j.Logger;

public class ChocolateStats {

    public static int chocoCounted = 0;
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void startCountingChocolate(int sec) {
        chocoCounted = 0;
        new Delay(sec, ChocolateStats::endCountingChocolate);
    }

    public static void endCountingChocolate() {
        LOGGER.info("Ended chocolate session with " + chocoCounted + " collected.");
    }

}
