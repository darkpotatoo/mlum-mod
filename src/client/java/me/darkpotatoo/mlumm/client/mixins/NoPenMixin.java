package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.Configuration;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class NoPenMixin {

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
    private void onPlay(SoundInstance sound, CallbackInfo ci) {
        Configuration config = AutoConfig.getConfigHolder(Configuration.class).getConfig();
        if (!config.nopen) return;
        Sound soundE = sound.getSound();
        if (soundE == null) return;
        Identifier id = soundE.getIdentifier();
        float pitch = soundE.getPitch().get(Random.create());

        if (id.getNamespace().contains("lever") && pitch > 1.0f) {
            ci.cancel();
        }
    }

}
