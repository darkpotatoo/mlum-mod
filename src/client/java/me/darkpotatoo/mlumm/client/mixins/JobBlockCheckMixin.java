package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.ui.ProgTrack;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class JobBlockCheckMixin {

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void onBlockClick(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        String b = MinecraftClient.getInstance().world.getBlockState(hitResult.getBlockPos()).getBlock().getName().toString();
        if (b.contains("dead_bush")) ProgTrack.rake();
        // TODO: the rest of these
    }
}