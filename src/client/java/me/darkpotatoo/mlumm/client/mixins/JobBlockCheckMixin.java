package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.ui.ProgTrack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class JobBlockCheckMixin {

    private long lastInteractionTime = 0;

    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    private void onBlockClick(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastInteractionTime < 10) return;

        String blockName = MinecraftClient.getInstance().world.getBlockState(hitResult.getBlockPos()).getBlock().getName().toString();
        if (blockName.contains("dead_bush")) ProgTrack.rake();
        lastInteractionTime = currentTime;
    }

    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    private void onBlockBreak(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastInteractionTime < 10) return;

        String blockName = MinecraftClient.getInstance().world.getBlockState(pos).getBlock().getName().toString();
        if (blockName.contains("dead_bush")) ProgTrack.rake();
        lastInteractionTime = currentTime;
    }
}