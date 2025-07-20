package me.darkpotatoo.mlumm.client.misc;

import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.ui.RiotMeter;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

public class AttackEventHandler {

    private static float lastTargetHealth = 0;

    public static void register() {
        AttackEntityCallback.EVENT.register(AttackEventHandler::onAttackEntity);
    }

    private static ActionResult onAttackEntity(PlayerEntity player, net.minecraft.world.World world, net.minecraft.util.Hand hand, Entity target, net.minecraft.util.hit.EntityHitResult hitResult) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return ActionResult.PASS;
        if (player != client.player) return ActionResult.PASS; // Only track local player attacks
        if (!(target instanceof LivingEntity livingTarget)) return ActionResult.PASS;

        // Track main hand item in arsenal
        ItemStack mainHandItem = player.getMainHandStack();
        if (!RiotMeter.arsenal.contains(mainHandItem)) RiotMeter.arsenal.add(mainHandItem);

        // Calculate damage difference
        float currentHealth = livingTarget.getHealth();
        double diff = lastTargetHealth - currentHealth;
        lastTargetHealth = currentHealth;

        // Reset combat ticks
        MlummClient.combatTicks = 100;

        if (diff >= 0.1) {
            RiotMeter.combo += 1;
            RiotMeter.combolastupdated = System.currentTimeMillis();
            if (RiotMeter.combo >= 2) {
                RiotMeter.add("+ COMBO x" + RiotMeter.combo, RiotMeter.combo * 10);
            }
        }

        if (diff > 10) RiotMeter.add("+ §cSTRONG HIT", 105);
        else if (diff > 0) RiotMeter.add(diff * 10);

        if (RiotTracker.isEnabled) {
            RiotTracker.hitsDealt++;
            RiotTracker.damageDealt += diff;
        }

        return ActionResult.PASS;
    }
}
