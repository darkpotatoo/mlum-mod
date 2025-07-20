package me.darkpotatoo.mlumm.client.mixins;

import com.mojang.authlib.GameProfile;
import me.darkpotatoo.mlumm.client.Configuration;
import me.darkpotatoo.mlumm.client.cape.CapeTextures;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.texture.PlayerSkinProvider;
import net.minecraft.client.util.SkinTextures;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Mixin(PlayerSkinProvider.class)
public class CapeMixin {

    @Unique
    private static final Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();

    @Inject(method = "fetchSkinTextures", at = @At("RETURN"), cancellable = true)
    private void onFetchSkinTextures(GameProfile profile, CallbackInfoReturnable<CompletableFuture<SkinTextures>> info) {
        if (!config.custom_cape) return;

        CompletableFuture<SkinTextures> originalFuture = info.getReturnValue();

        CompletableFuture<SkinTextures> modifiedFuture = originalFuture.thenApply(original -> {
            return new SkinTextures(
                    original.texture(),
                    original.textureUrl(),
                    CapeTextures.getCapeTexture(), // Your custom cape
                    original.elytraTexture(),
                    original.model(),
                    original.secure()
            );
        });

        info.setReturnValue(modifiedFuture);
    }

//        @Inject(method = "fetchSkinTextures", at = @At("RETURN"), cancellable = true)
//        private void onFetchSkinTextures(GameProfile profile, CallbackInfoReturnable<CompletableFuture<Optional<SkinTextures>>> info) {
//            if (!config.custom_cape) return;
//
//            CompletableFuture<Optional<SkinTextures>> originalFuture = info.getReturnValue();
//
//            CompletableFuture<Optional<SkinTextures>> modifiedFuture = originalFuture.thenApply(optionalTextures -> {
//                if (optionalTextures.isEmpty()) return optionalTextures;
//
//                SkinTextures original = optionalTextures.get();
//                SkinTextures modified = new SkinTextures(
//                        original.texture(),
//                        original.textureUrl(),
//                        CapeTextures.getCapeTexture(), // cape
//                        original.elytraTexture(),
//                        original.model(),
//                        original.secure()
//                );
//
//                return Optional.of(modified);
//            });
//
//            info.setReturnValue(modifiedFuture);
//        }
}