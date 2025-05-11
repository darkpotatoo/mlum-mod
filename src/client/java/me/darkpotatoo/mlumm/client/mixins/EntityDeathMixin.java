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

@Mixin(LivingEntity.class)
public abstract class EntityDeathMixin {

    @Inject(method = "onDeath", at = @At("TAIL"))
    private void onEntityDeath(DamageSource source, CallbackInfo ci) {
        if (source.getAttacker() instanceof PlayerEntity attacker) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && attacker.getName().getString().equals(client.player.getName().getString())) {
                LivingEntity entity = (LivingEntity) (Object) this;
                for (ItemStack armorPiece : entity.getArmorItems()) {
                    //armorPiece.getName().getString(); TODO: make ts work with special upgrades
                }

                MlummClient.combatTicks = 0;
                RiotTracker.kills++;
                System.out.println("e");
                RiotMeter.add("+ §fKILL", 200);
            }
        }
    }
}