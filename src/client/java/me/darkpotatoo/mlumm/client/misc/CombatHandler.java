package me.darkpotatoo.mlumm.client.misc;

import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.ui.RiotMeter;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

public class CombatHandler {

    private static LivingEntity lastDamagedTarget = null;
    private static float lastTargetHealth = -1f; // Track target health to detect actual damage
    private static long lastDamageTime = 0;

    private static long lastKillTime = 0;
    private static int killStreak = 0;

    private static float lastSelfHealth = -1;

    public static void register() {
        AttackEntityCallback.EVENT.register(CombatHandler::onAttackEntity);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // Track player health to reset combo if damaged
            float currentHealth = client.player.getHealth();
            if (lastSelfHealth < 0) {
                lastSelfHealth = currentHealth;
            } else if (currentHealth < lastSelfHealth) {
                RiotMeter.combo = 0;
                RiotTracker.damageTaken += lastSelfHealth - currentHealth;
            }
            lastSelfHealth = currentHealth;

            // Check if last damaged target took damage
            if (lastDamagedTarget != null && lastTargetHealth >= 0) {
                float currentTargetHealth = lastDamagedTarget.getHealth();

                if (currentTargetHealth < lastTargetHealth) {
                    // Actual damage dealt, increment combo
                    RiotMeter.combo += 1;
                    RiotMeter.combolastupdated = System.currentTimeMillis();

                    if (RiotMeter.combo >= 2)
                        RiotMeter.add("+ COMBO x" + RiotMeter.combo, RiotMeter.combo * 2);

                    if (RiotTracker.isEnabled)
                        RiotTracker.hitsDealt++;

                    MlummClient.combatTicks = 100;
                    RiotMeter.add(8);

                    lastTargetHealth = -1;
                    lastDamagedTarget = null;
                } else if (System.currentTimeMillis() - lastDamageTime > 1000) {
                    lastTargetHealth = -1;
                    lastDamagedTarget = null;
                }
            }
        });
    }

    private static ActionResult onAttackEntity(PlayerEntity player, World world, Hand hand, Entity target, EntityHitResult hitResult) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || player != client.player
                || !((target instanceof PlayerEntity) || (target instanceof ZombieEntity))) {
            return ActionResult.PASS;
        }

        LivingEntity livingTarget = (LivingEntity) target;

        lastDamagedTarget = livingTarget;
        lastTargetHealth = livingTarget.getHealth();
        lastDamageTime = System.currentTimeMillis();

        ItemStack mainHandItem = player.getMainHandStack();
        if (!RiotMeter.arsenal.contains(mainHandItem)) RiotMeter.arsenal.add(mainHandItem);

        return ActionResult.PASS;
    }

    public static void onKnockoutDetected(PlayerEntity player) {
        if (lastDamagedTarget == null) return;
        if (System.currentTimeMillis() - lastDamageTime > 10000) return;

        LivingEntity target = lastDamagedTarget;

        long now = System.currentTimeMillis();
        if (now - lastKillTime <= 8000) killStreak++;
        else killStreak = 1;

        if (now - lastKillTime <= 15000 && now - lastKillTime >= 2000 && killStreak >= 3)
            RiotMeter.add("+ §aKILLSTREAK", 0);

        lastKillTime = now;

        if (player.getMainHandStack().getName().getString().contains("Air"))
            RiotMeter.add("+ §aFISTKILL", 60);

        String killMessage = switch (killStreak) {
            case 1 -> "KILL";
            case 2 -> "§aDOUBLE KILL";
            case 3 -> "§eTRIPLE KILL";
            case 4 -> "§eQUADRA KILL";
            case 5 -> "§6PENTA KILL";
            default -> "§cMULTI KILL (" + killStreak + ")";
        };

        MlummClient.combatTicks = 0;
        RiotMeter.combo = 0;
        RiotTracker.kills++;

        RiotMeter.add("+ " + killMessage, 40 + (killStreak * 30));

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = target.getEquippedStack(slot);
            if (stack.getName().getString().contains("Iron")) RiotMeter.add(10);
            if (stack.getName().getString().contains("Detective")) RiotTracker.detKills++;
            if (stack.getName().getString().contains("Guard")) RiotTracker.guardKills++;
            if (stack.getName().getString().contains("Trainee")) RiotTracker.traineeKills++;
        }

        // Reset
        lastDamagedTarget = null;
        lastTargetHealth = -1;
    }
}
