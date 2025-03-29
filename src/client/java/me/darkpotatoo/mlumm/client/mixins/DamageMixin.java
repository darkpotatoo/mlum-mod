package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.statistical.RiotTracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class DamageMixin extends LivingEntity {

    protected DamageMixin() {
        super(null, null);
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (amount != 0) {
            MlummClient.combatTicks = 100;
        }
        if (RiotTracker.isEnabled) {
            RiotTracker.damageTaken += amount;
        }
    }

    @Inject(method = "attack", at = @At("HEAD"))
    private void onAttack(Entity target, CallbackInfo ci) {
        MlummClient.combatTicks = 100;
        if (RiotTracker.isEnabled) {
            MinecraftClient client = MinecraftClient.getInstance();
            float dmg = this.isUsingRiptide() ? this.riptideAttackDamage : (float) getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            DamageSource dmgSource = this.getDamageSources().playerAttack(client.player);
            RiotTracker.damageDealt += client.player.getWeaponStack().getItem().getBonusAttackDamage(target, dmg, dmgSource);
            System.out.println(RiotTracker.damageDealt);
        }
    }

}