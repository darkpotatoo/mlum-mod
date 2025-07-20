//package me.darkpotatoo.mlumm.client.mixins;
//
//import me.darkpotatoo.mlumm.client.MlummClient;
//import me.darkpotatoo.mlumm.client.ui.RiotMeter;
//import me.darkpotatoo.mlumm.client.misc.RiotTracker;
//import net.minecraft.client.MinecraftClient;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EquipmentSlot;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.entity.damage.DamageSource;
//import net.minecraft.entity.player.PlayerEntity;
//import net.minecraft.item.ItemStack;
//import net.minecraft.server.world.ServerWorld;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.Arrays;
//
//@Mixin(PlayerEntity.class)
//public abstract class DamageMixin extends LivingEntity {
//
//    @Unique
//    private float hpbefore = 0;
//
//    protected DamageMixin() {
//        super(null, null);
//    }
//
//    @Inject(method = "damage", at = @At("HEAD"))
//    private void onDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
//        MinecraftClient client = MinecraftClient.getInstance();
//        if (client == null || client.player == null) return;
//        if ((PlayerEntity)(Object)this != (PlayerEntity)client.player) return;
//        if (!client.isOnThread()) return;
//
//        if (amount != 0) {
//            MlummClient.combatTicks = 100;
//        }
//        if (RiotTracker.isEnabled) {
//            RiotTracker.hitsTaken++;
//            RiotTracker.damageTaken += amount;
//        }
//        RiotMeter.combo = 0;
//    }
//
//    @Inject(method = "attack", at = @At("HEAD"))
//    private void onAttackHead(Entity target, CallbackInfo ci) {
//        MinecraftClient client = MinecraftClient.getInstance();
//        if (client == null || client.player == null) return;
//        if ((PlayerEntity)(Object)this != (PlayerEntity)client.player) return;
//        if (!client.isOnThread()) return;
//
//        ItemStack item = client.player.getMainHandStack();
//        if (!RiotMeter.arsenal.contains(item)) RiotMeter.arsenal.add(item);
//
//        if (!(target instanceof LivingEntity)) return;
//        LivingEntity livingTarget = (LivingEntity) target;
//
//        hpbefore = livingTarget.getHealth();
//        if (hpbefore < 1 && RiotMeter.combo == 0) RiotMeter.add("+ §7CLEANED", 20);
//    }
//
//    @Inject(method = "attack", at = @At("TAIL"))
//    private void onAttackTail(Entity target, CallbackInfo ci) {
//        MinecraftClient client = MinecraftClient.getInstance();
//        if (client == null || client.player == null) return;
//        if ((PlayerEntity)(Object)this != (PlayerEntity)client.player) return;
//        if (!client.isOnThread()) return;
//
//        MlummClient.combatTicks = 100;
//
//        if (!(target instanceof LivingEntity)) return;
//        LivingEntity livingTarget = (LivingEntity) target;
//
//        double diff = hpbefore - livingTarget.getHealth();
//
//        if (diff >= 0.1) {
//            RiotMeter.combo += 1;
//            RiotMeter.combolastupdated = System.currentTimeMillis();
//            if (RiotMeter.combo >= 2) {
//                RiotMeter.add("+ COMBO x" + RiotMeter.combo, RiotMeter.combo * 10);
//            }
//        }
//
//        if (diff > 10) RiotMeter.add("+ §cSTRONG HIT", 105);
//        else RiotMeter.add(diff * 10);
//
//        if (RiotTracker.isEnabled) {
//            RiotTracker.hitsDealt++;
//            RiotTracker.damageDealt += diff;
//        }
//    }
//}
