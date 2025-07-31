package me.darkpotatoo.mlumm.client;

import me.darkpotatoo.mlumm.client.cape.CapeTexture;
import me.darkpotatoo.mlumm.client.misc.NotifType;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

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
    public boolean timer_fugbox = true;
    @ConfigEntry.Category("Notifs")
    @ConfigEntry.Gui.Tooltip
    public NotifType notif_combat = NotifType.MINIMAL;
    @ConfigEntry.Category("Notifs")
    @ConfigEntry.Gui.Tooltip
    public NotifType notif_desk = NotifType.MINIMAL;
    @ConfigEntry.Category("Notifs")
    @ConfigEntry.Gui.Tooltip
    public NotifType notif_box = NotifType.MINIMAL;
    @ConfigEntry.Category("Notifs")
    @ConfigEntry.Gui.Tooltip
    public NotifType notif_fugbox = NotifType.MINIMAL;
    @ConfigEntry.Category("Notifs")
    @ConfigEntry.Gui.Tooltip
    public NotifType notif_fishing = NotifType.MINIMAL;
    @ConfigEntry.Gui.Tooltip
    public boolean contraband_tooltip = true;
    @ConfigEntry.Gui.Tooltip
    public boolean toasts = true;
    @ConfigEntry.Gui.Tooltip
    public boolean fishing_alert = true;
    @ConfigEntry.Gui.Tooltip
    public boolean actionbar_status = true;
    @ConfigEntry.Gui.Tooltip
    public CapeTexture cape_texture = CapeTexture.MLUM_S16;
    @ConfigEntry.Gui.Tooltip
    public boolean custom_cape = false;
    @ConfigEntry.Gui.Tooltip
    public boolean stylemeter = true;
    @ConfigEntry.Gui.Tooltip
    public boolean astylemeter = false;
    @ConfigEntry.Gui.Tooltip
    public boolean nopen = true;
    @ConfigEntry.Gui.Tooltip
    public boolean itemcosts = true;
    @ConfigEntry.Gui.Tooltip
    public boolean escsounds = true;
    @ConfigEntry.Gui.Tooltip
    public boolean chatmode = true;
    @ConfigEntry.Gui.Tooltip
    public boolean rolenotifs = true;
    @ConfigEntry.BoundedDiscrete(min = 200, max = 1500)
    @ConfigEntry.Gui.Tooltip
    public int bmdelay = 400;
    public int riotmeter_x = 10;
    public int riotmeter_y = 10;
    @ConfigEntry.BoundedDiscrete(min = 50, max = 400)
    @ConfigEntry.Gui.Tooltip
    public int riotmeter_width = 100;
    @ConfigEntry.Gui.Tooltip
    @ConfigEntry.BoundedDiscrete(min = 50, max = 400)
    public int riotmeter_height = 200;

}

