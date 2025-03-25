package me.darkpotatoo.mlumm.client;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "mlumm")
public class Configuration implements ConfigData {

    public static boolean timer_combat = true;
    public static boolean timer_desk = true;
    public static boolean timer_crate = true;
    public static boolean timer_box = true;
    public static boolean timer_trash = true;
    public static boolean timer_mail = true;

}