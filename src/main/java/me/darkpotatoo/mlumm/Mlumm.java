package me.darkpotatoo.mlumm;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

public class Mlumm implements ModInitializer {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOGGER.warn("i am hacker and it is too late for your computer muhahahahhahaha");
    }
}
