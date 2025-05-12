package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.riot.RiotMeter;
import me.darkpotatoo.mlumm.client.riot.RiotTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList;

@Mixin(LivingEntity.class)
public abstract class EntityDeathMixin {

    private static long lastKillTime = 0;
    private static int killStreak = 0;

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onEntityDeath(DamageSource source, CallbackInfo ci) {
        if (source.getAttacker() instanceof PlayerEntity attacker) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && attacker.getName().getString().equals(client.player.getName().getString())) {

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastKillTime <= 8000) {
                    killStreak++;
                } else {
                    killStreak = 1;
                }
                lastKillTime = currentTime;

                if (RiotMeter.tryArsenal()) RiotMeter.add("+ §bARSENAL", 80);

                String killMessage;
                switch (killStreak) {
                    case 1 -> killMessage = "KILL";
                    case 2 -> killMessage = "§aDOUBLE KILL";
                    case 3 -> killMessage = "§aTRIPLE KILL";
                    case 4 -> killMessage = "§eQUADRUPLE KILL";
                    case 5 -> killMessage = "§ePENTA KILL";
                    default -> killMessage = "§6MULTI KILL (" + killStreak + ")";
                }

                MlummClient.combatTicks = 0;
                RiotMeter.combo = 0;
                RiotTracker.kills++;
                RiotMeter.add("+ " + killMessage, 120 + (killStreak * 30));
            }
        }
    }
}