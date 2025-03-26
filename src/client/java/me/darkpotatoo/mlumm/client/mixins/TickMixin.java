package me.darkpotatoo.mlumm.client.mixins;

import com.mojang.logging.LogUtils;
import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
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

import java.lang.reflect.Field;

@Mixin(MinecraftClient.class)
public class TickMixin {

    @Shadow @Final private ToastManager toastManager;
    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        // Escape announcement cooldown
        MlummClient.escapeTicks--;

        //Desk timer
        if (MlummClient.deskTicks > 0 && config.timer_desk) {
            MlummClient.deskTicks--;
            if (MlummClient.deskTicks == 0) {
                player.sendMessage(Text.of("§a» §fDesk timer has ended"), false);
                sendCustomToast(client, "Desk Timer Ended", "You can now open a desk again");
            }
        }

        //Fugitive crate timer
        if (MlummClient.crateTicks > 0 && config.timer_crate) {
            MlummClient.crateTicks--;
            if (MlummClient.crateTicks == 0) {
                player.sendMessage(Text.of("§a» §fCrate timer has ended"), false);
                sendCustomToast(client, "Crate Timer Ended", "You can now open a crate again");
            }
        }

        // Fugitive contra box timer
        if (MlummClient.boxTicks > 0 && config.timer_box) {
            MlummClient.boxTicks--;
            if (MlummClient.boxTicks == 0) {
                player.sendMessage(Text.of("§a» §fContraband Box has ended"), false);
                sendCustomToast(client, "Contraband Box Timer Ended", "You can now get a contraband box again");
            }
        }

        // Mail timer
        if (MlummClient.mailTicks > 0 && config.timer_mail) {
            MlummClient.mailTicks--;
            if (MlummClient.mailTicks == 0) {
                player.sendMessage(Text.of("§a» §fMail timer has ended"), false);
                sendCustomToast(client, "Mail Timer Ended", "You can now open a crate again");
            }
        }

        // Trash timer
        if (MlummClient.trashTicks > 0 && config.timer_trash) {
            MlummClient.trashTicks--;
            if (MlummClient.trashTicks == 0) {
                player.sendMessage(Text.of("§a» §fTrash timer has ended"), false);
                sendCustomToast(client, "Trash Timer Ended", "You can now get a trash bag again");
            }
        }

        //combat timer
        if (MlummClient.combatTicks > 0  && config.timer_combat) {
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