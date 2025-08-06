package me.darkpotatoo.mlumm.client.mixins;

import com.mojang.authlib.GameProfile;
import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.MlummClient;
import me.darkpotatoo.mlumm.client.cape.CapeTextures;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.PlayerCapeModel;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class CapeMixin {

    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    private void onGetSkinTextures(CallbackInfoReturnable<SkinTextures> cir) {
        if (!AutoConfig.getConfigHolder(Configuration.class).getConfig().custom_cape) return;
        AbstractClientPlayerEntity self = (AbstractClientPlayerEntity)(Object)this;
        if (self == MinecraftClient.getInstance().player) {
            SkinTextures original = cir.getReturnValue();
            SkinTextures modified = new SkinTextures(
                    original.texture(),
                    original.textureUrl(),
                    CapeTextures.getCapeTexture(),
                    original.elytraTexture(),
                    original.model(),
                    original.secure()
            );
            cir.setReturnValue(modified);
        }
    }
}