package me.darkpotatoo.mlumm.client.misc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.text.Text;

public class UtilMethods {

    public static void sendCustomToast(String text, String text2) {
        ToastManager toastManager = MinecraftClient.getInstance().getToastManager();
        toastManager.add(new SystemToast(SystemToast.Type.WORLD_BACKUP, Text.of(text), Text.of(text2)));
    }

    // trust ill add more to this and this file will be useful

}
