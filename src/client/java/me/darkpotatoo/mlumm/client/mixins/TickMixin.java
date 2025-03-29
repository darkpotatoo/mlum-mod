package me.darkpotatoo.mlumm.client.mixins;

import com.mojang.logging.LogUtils;
import me.darkpotatoo.mlumm.Mlumm;
import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.statistical.ChocolateStats;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Mixin(MinecraftClient.class)
public class TickMixin {

    @Shadow @Final private ToastManager toastManager;
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Map<Integer, ItemStack> previousInventory = new HashMap<>();

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        // chocolate diff for chocostats
        if (player != null) {
            PlayerInventory inventory = player.getInventory();
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack currentStack = inventory.getStack(i);
                ItemStack previousStack = previousInventory.getOrDefault(i, ItemStack.EMPTY);
                if (!ItemStack.areEqual(currentStack, previousStack)) {
                    if (currentStack.getItem().getName().getString().contains("Nether Brick")) {
                        ChocolateStats.chocoCounted += currentStack.getCount();
                    }
                    previousInventory.put(i, currentStack.copy());
                }
            }
        }

        // fishing warning
        MlummClient.rodTicks--;
        if (client.player != null) {
            if (client.player.getMainHandStack().getName().getString().contains("Rod") && client.player.getMainHandStack().getMaxDamage() - client.player.getMainHandStack().getDamage() <= 3 && MlummClient.rodTicks <= 0 && config.fishing_alert) {
                MinecraftClient.getInstance().getToastManager().add(new SystemToast(SystemToast.Type.WORLD_BACKUP, Text.literal("Fishing Rod Warning"), Text.literal("Your fishing rod is about to break")));
                client.player.playSound(SoundEvents.ENTITY_GHAST_WARN);
                MlummClient.rodTicks = 300;
            }
        }

        // Escape announcement cooldown
        MlummClient.escapeTicks--;

        //Desk timer
        if (MlummClient.deskTicks > 0 && config.timer_desk) {
            MlummClient.deskTicks--;
            if (MlummClient.deskTicks == 0) {
                player.sendMessage(Text.of("§a» §fDesk timer has ended"), false);
                sendCustomToast(client, "Desk Delay Ended", "You can now open a desk again");
            }
        }

        //Fugitive crate timer
        if (MlummClient.crateTicks > 0 && config.timer_crate) {
            MlummClient.crateTicks--;
            if (MlummClient.crateTicks == 0) {
                player.sendMessage(Text.of("§a» §fCrate timer has ended"), false);
                sendCustomToast(client, "Crate Delay Ended", "You can now open a crate again");
            }
        }

        // Fugitive contra box timer
        if (MlummClient.boxTicks > 0 && config.timer_box) {
            MlummClient.boxTicks--;
            if (MlummClient.boxTicks == 0) {
                player.sendMessage(Text.of("§a» §fContraband Box has ended"), false);
                sendCustomToast(client, "Contraband Box Delay Ended", "You can now get a contraband box again");
            }
        }

        // Mail timer
        if (MlummClient.mailTicks > 0 && config.timer_mail) {
            MlummClient.mailTicks--;
            if (MlummClient.mailTicks == 0) {
                player.sendMessage(Text.of("§a» §fMail timer has ended"), false);
                sendCustomToast(client, "Mail Delay Ended", "You can now open a crate again");
            }
        }

        // Trash timer
        if (MlummClient.trashTicks > 0 && config.timer_trash) {
            MlummClient.trashTicks--;
            if (MlummClient.trashTicks == 0) {
                player.sendMessage(Text.of("§a» §fTrash timer has ended"), false);
                sendCustomToast(client, "Trash Delay Ended", "You can now get a trash bag again");
            }
        }

        //combat timer
        if (MlummClient.combatTicks > 0  && config.timer_combat) {
            MlummClient.combatTicks--;
            if (MlummClient.combatTicks == 0) {
                player.sendMessage(Text.of("§a» §fCombat timer has ended"), false);
                sendCustomToast(client, "Combat Delay Ended", "You are now out of combat");
            }
        }
    }

    //custom toast
    private void sendCustomToast(MinecraftClient client, String text, String text2) {
        ToastManager toastManager = client.getToastManager();
        toastManager.add(new SystemToast(SystemToast.Type.WORLD_BACKUP, Text.of(text), Text.of(text2)));
    }
}