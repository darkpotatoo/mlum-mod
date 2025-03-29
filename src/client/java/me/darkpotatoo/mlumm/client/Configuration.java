package me.darkpotatoo.mlumm.client;

import me.darkpotatoo.mlumm.client.util.escSound;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "mlumm")
public class Configuration implements ConfigData {

    @ConfigEntry.Gui.Tooltip
    public boolean timer_combat = true;
    @ConfigEntry.Gui.Tooltip
    public boolean timer_desk = true;
    @ConfigEntry.Gui.Tooltip
    public boolean timer_crate = true;
    @ConfigEntry.Gui.Tooltip
    public boolean timer_box = true;
    @ConfigEntry.Gui.Tooltip
    public boolean timer_trash = true;
    @ConfigEntry.Gui.Tooltip
    public boolean timer_mail = true;
    @ConfigEntry.Gui.Tooltip
    public boolean contraband_tooltip = true;
    @ConfigEntry.Gui.Tooltip
    public boolean fishing_alert = true;
    @ConfigEntry.Gui.Tooltip
    public escSound escape_sound = escSound.AMETHYST;

}

