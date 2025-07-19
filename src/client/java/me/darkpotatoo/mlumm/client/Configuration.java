package me.darkpotatoo.mlumm.client;

import me.darkpotatoo.mlumm.client.cape.CapeTexture;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

@Config(name = "mlumm")
public class Configuration implements ConfigData {

    @ConfigEntry.Category("Timers")
    @ConfigEntry.Gui.Tooltip
    public boolean timer_combat = true;
    @ConfigEntry.Category("Timers")
    @ConfigEntry.Gui.Tooltip
    public boolean timer_desk = true;
    @ConfigEntry.Category("Timers")
    @ConfigEntry.Gui.Tooltip
    public boolean timer_crate = true;
    @ConfigEntry.Category("Timers")
    @ConfigEntry.Gui.Tooltip
    public boolean timer_box = true;
    @ConfigEntry.Category("Timers")
    @ConfigEntry.Gui.Tooltip
    public boolean timer_trash = true;
    @ConfigEntry.Category("Timers")
    @ConfigEntry.Gui.Tooltip
    public boolean timer_mail = true;
    @ConfigEntry.Gui.Tooltip
    public boolean contraband_tooltip = true;
    @ConfigEntry.Gui.Tooltip
    public boolean toasts = true;
    @ConfigEntry.Gui.Tooltip
    public boolean fishing_alert = true;
    @ConfigEntry.Gui.Tooltip
    public boolean actionbar_status = true;
    @ConfigEntry.Gui.Tooltip
    public SoundEvent escape_sound = SoundEvents.BLOCK_NOTE_BLOCK_BELL.value();
    @ConfigEntry.Gui.Tooltip
    public CapeTexture cape_texture = CapeTexture.MLUM_S16;
    @ConfigEntry.Gui.Tooltip
    public boolean custom_cape = true;
    @ConfigEntry.Gui.Tooltip
    public boolean stylemeter = true;
    @ConfigEntry.Gui.Tooltip
    public boolean itemcosts = true;
    @ConfigEntry.Gui.Tooltip
    public boolean escsounds = true;
    @ConfigEntry.Gui.Tooltip
    public boolean chatmode = true;
    @ConfigEntry.Gui.Tooltip
    public boolean rolenotifs = true;
    @ConfigEntry.Gui.Tooltip
    public boolean progtrack = true;
    @ConfigEntry.BoundedDiscrete(min = -100, max = 100)
    @ConfigEntry.Gui.Tooltip
    public int progtrackhigh = 0;
    @ConfigEntry.BoundedDiscrete(min = 200, max = 1500)
    @ConfigEntry.Gui.Tooltip
    public int bmdelay = 400;


}

