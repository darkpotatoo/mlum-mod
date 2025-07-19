package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.ui.RiotMeter;
import me.darkpotatoo.mlumm.client.misc.RiotTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class EntityDeathMixin {

    @Unique
    private static long lastKillTime = 0;
    @Unique
    private static int killStreak = 0;
    @Unique
    private static Iterable<ItemStack> deadgear;

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
                if (currentTime - lastKillTime <= 15000 && currentTime-lastKillTime >= 2000 && killStreak >= 3) RiotMeter.add("+ §aKILLSTREAK", 0);
                lastKillTime = currentTime;

                if (RiotMeter.tryArsenal()) RiotMeter.add("+ §bARSENAL", 80);
                if (client.player.getMainHandStack().getName().getString().contains("Air")) RiotMeter.add("+ §aFISTKILL", 60);

                String killMessage;
                switch (killStreak) {
                    case 1 -> killMessage = "KILL";
                    case 2 -> killMessage = "§aDOUBLE KILL";
                    case 3 -> killMessage = "§eTRIPLE KILL";
                    case 4 -> killMessage = "§eQUADRA KILL";
                    case 5 -> killMessage = "§6PENTA KILL";
                    default -> killMessage = "§cMULTI KILL (" + killStreak + ")";
                }

                MlummClient.combatTicks = 0;
                RiotMeter.combo = 0;
                RiotTracker.kills++;
                RiotMeter.add("+ " + killMessage, 120 + (killStreak * 30));
            }
            // gear pts
            deadgear = ((LivingEntity) source.getAttacker()).getEquippedItems();
            deadgear.forEach(itemStack -> {
                if (itemStack.getName().getString().contains("Iron")) {
                    RiotMeter.add(10); // 10 pts per iron item like guard chest and stuff
                }
            });
        }
    }
}