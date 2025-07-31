package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.misc.ChocolateStats;
import me.darkpotatoo.mlumm.client.misc.UtilMethods;
import me.darkpotatoo.mlumm.client.ui.RiotMeter;
import me.darkpotatoo.mlumm.client.misc.RiotTracker;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.HashMap;
import java.util.Map;

@Mixin(MinecraftClient.class)
public class TickMixin {

    @Unique
    private final Map<Integer, ItemStack> previousInventory = new HashMap<>();
    @Unique
    Configuration config;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo info) {
        config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        // chocolate diff for chocolate stats and item diff for ldk gear pickups
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
                UtilMethods.notify(config.notif_fishing, "Rod Durability Warning", "Your fishing rod is about to break!", "§c");
                client.player.playSound(SoundEvents.ENTITY_GHAST_WARN);
                MlummClient.rodTicks = 300;
            }
        }

        // Riot style meter decay
        RiotMeter.decayScore();
        RiotMeter.resetComboCheck();

        // Riot time
        RiotTracker.riotTicks++;

        // Escape announcement cooldown
        MlummClient.escapeTicks--;

        //Desk timer
        if (MlummClient.deskTicks > 0 && config.timer_desk) {
            MlummClient.deskTicks--;
            if (MlummClient.deskTicks == 0) {
                UtilMethods.notify(config.notif_desk, "Desk Timer Ended", "You can now open a desk again", "§a");
            }
        }

        //Fugitive crate timer
        if (MlummClient.crateTicks > 0 && config.timer_crate) {
            MlummClient.crateTicks--;
            if (MlummClient.crateTicks == 0) {
                UtilMethods.notify(config.notif_box, "Crate Timer Ended", "You can now open a crate again", "§a");
            }
        }

        //Fugitive box timer
        if (MlummClient.boxTicks > 0 && config.timer_fugbox) {
            MlummClient.boxTicks--;
            if (MlummClient.boxTicks == 0) {
                UtilMethods.notify(config.notif_fugbox, "Smuggling Box Timer Ended", "You can now get a box again", "§a");
            }
        }

        //combat timer
        if (MlummClient.combatTicks > 0  && config.timer_combat) {
            MlummClient.combatTicks--;
            if (MlummClient.combatTicks == 0) {
                UtilMethods.notify(config.notif_combat, "Combat Timer Ended", "You are now out of combat", "§a");
            }
        }
    }
}