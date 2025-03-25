package me.darkpotatoo.mlumm.client.mixins;

import com.mojang.logging.LogUtils;
import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.MlummClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class TickMixin {

    @Shadow @Final private ToastManager toastManager;
    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        //Desk timer
        if (MlummClient.deskTicks > 0 && Configuration.timer_desk) {
            MlummClient.deskTicks--;
            if (MlummClient.deskTicks == 0) {
                player.sendMessage(Text.of("§a» §fDesk timber has ended"), false);
                sendCustomToast(client, "Desk Timer Ended", "You can now open a desk again");
            }
        }

        //Fugitive crate timer
        if (MlummClient.crateTicks > 0 && Configuration.timer_crate) {
            MlummClient.crateTicks--;
            if (MlummClient.crateTicks == 0) {
                player.sendMessage(Text.of("§a» §fCrate timber has ended"), false);
                sendCustomToast(client, "Crate Timer Ended", "You can now open a crate again");
            }
        }

        // Fugitive contra box timer
        if (MlummClient.boxTicks > 0 && Configuration.timer_box) {
            MlummClient.boxTicks--;
            if (MlummClient.boxTicks == 0) {
                player.sendMessage(Text.of("§a» §fCrate timber has ended"), false);
                sendCustomToast(client, "Crate Timer Ended", "You can now open a crate again");
            }
        }

        // Mail timer
        if (MlummClient.mailTicks > 0 && Configuration.timer_mail) {
            MlummClient.mailTicks--;
            if (MlummClient.mailTicks == 0) {
                player.sendMessage(Text.of("§a» §fCrate timber has ended"), false);
                sendCustomToast(client, "Crate Timer Ended", "You can now open a crate again");
            }
        }

        // Trash timer
        if (MlummClient.trashTicks > 0 && Configuration.timer_trash) {
            MlummClient.trashTicks--;
            if (MlummClient.trashTicks == 0) {
                player.sendMessage(Text.of("§a» §fCrate timber has ended"), false);
                sendCustomToast(client, "Crate Timer Ended", "You can now open a crate again");
            }
        }

        //combat timer
        if (MlummClient.combatTicks > 0  && Configuration.timer_combat) {
            MlummClient.combatTicks--;
            if (MlummClient.combatTicks == 0) {
                player.sendMessage(Text.of("§a» §fCombat timer has ended"), false);
                sendCustomToast(client, "Combat Timer Ended", "You are now out of combat");
            }
        }
    }

    //custom toast
    private void sendCustomToast(MinecraftClient client, String text, String text2) {
        ToastManager toastManager = client.getToastManager();
        toastManager.add(new SystemToast(SystemToast.Type.WORLD_BACKUP, Text.of(text), Text.of(text2)));
    }
}