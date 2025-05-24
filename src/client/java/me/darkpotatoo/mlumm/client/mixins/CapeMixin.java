package me.darkpotatoo.mlumm.client.mixins;

import com.mojang.authlib.GameProfile;
import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.cape.CapeTextures;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@Mixin(PlayerSkinProvider.class)
public class CapeMixin {

    private static final Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

    @Inject(method = "fetchSkinTextures*", at = @At("RETURN"), cancellable = true)
    private void onFetchSkinTextures(GameProfile profile, CallbackInfoReturnable<CompletableFuture<SkinTextures>> info) {
        if (!config.custom_cape) return;
        CompletableFuture<SkinTextures> modifiedTextures = info.getReturnValue().thenApplyAsync(originalTextures -> new SkinTextures(
                originalTextures.texture(),
                originalTextures.textureUrl(),
                CapeTextures.getCapeTexture(),
                originalTextures.elytraTexture(),
                originalTextures.model(),
                originalTextures.secure()
        ));

        info.setReturnValue(modifiedTextures);
    }
}