package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.statistical.RiotTracker;
import me.darkpotatoo.mlumm.client.util.Delay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class DamageMixin extends LivingEntity {

    private float hpbefore = 0;
    private LivingEntity entitybefore;
    private Iterable<ItemStack> entitygear;
    private int rolebyhelmet; // 0 = ld det or ld trainee, 1 = ld guard
    private int rolebychestplate; // 0 = det, 1 = guard/ld trainee 2 = trainee

    protected DamageMixin() {
        super(null, null);
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (amount != 0) {
            MlummClient.combatTicks = 100;
        }
        if (RiotTracker.isEnabled) {
            RiotTracker.hitsTaken++;
            RiotTracker.damageTaken += amount;
        }
    }

    @Inject(method = "attack", at = @At("HEAD"))
    private void onAttackTwo(Entity target, CallbackInfo ci) {
        hpbefore = ((LivingEntity) target).getHealth();
        entitybefore = (LivingEntity) target;
        entitygear = ((LivingEntity) target).getEquippedItems();
    }

    @Inject(method = "attack", at = @At("TAIL"))
    private void onAttack(Entity target, CallbackInfo ci) {
        MlummClient.combatTicks = 100;
        RiotTracker.hitsDealt++;
        double diff = hpbefore - ((LivingEntity) target).getHealth();
        RiotTracker.damageDealt += diff;
        if (((LivingEntity) target).getHealth() == 0) {
            RiotTracker.kills++;
            MlummClient.combatTicks = 1;
            for (ItemStack thing : entitygear) {} // TODO: MAke this actually work with the s16 gear sets
        }
    }
}