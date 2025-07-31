package me.darkpotatoo.mlumm.client.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;

public class UtilMethods {

    // trust ill add more to this and this file will be useful

    public static void notify(NotifType strong, String title, String content, String color) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;
        if (strong == NotifType.LOUD) {
            client.inGameHud.setTitle(Text.of(color + title));
            client.inGameHud.setSubtitle(Text.of(content));
            ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
            toastManager.add(new SystemToast(SystemToast.Type.WORLD_BACKUP, Text.of(title), Text.of(content)));
            client.player.sendMessage(Text.of(color + "» §f" + content), false);
        } else if (strong == NotifType.NORMAL) {
            ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
            toastManager.add(new SystemToast(SystemToast.Type.WORLD_BACKUP, Text.of(title), Text.of(content)));
            client.player.sendMessage(Text.of(color + "» §f" + content), false);
        } else if (strong == NotifType.MINIMAL) {
            client.player.sendMessage(Text.of(color + "» §f" + content), false);
        }
    }

}

