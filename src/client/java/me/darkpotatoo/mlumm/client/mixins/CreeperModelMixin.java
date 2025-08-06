package me.darkpotatoo.mlumm.client.mixins;

import me.darkpotatoo.mlumm.client.ui.ChatModeSelector;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.client.render.entity.state.CreeperEntityRenderState;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CreeperEntityModel.class)

public abstract class CreeperModelMixin {

	/**
	 * @author hotpocket
	 * @reason removing everything but the body so it looks nicer
	 */
	@Overwrite
	public static TexturedModelData getTexturedModelData(Dilation dilation) {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 0).cuboid(0F, 0F, 0F, 0F, 0F, 0F, dilation), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-6.1F, -6.1F, -3F, 12.2F, 28.2F, 6F, dilation), ModelTransform.pivot(0.0F, 3.9F, 0.0F));
		ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(0, 16).cuboid(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, dilation);
		modelPartData.addChild("right_hind_leg", modelPartBuilder, ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild("left_hind_leg", modelPartBuilder, ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild("right_front_leg", modelPartBuilder, ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		modelPartData.addChild("left_front_leg", modelPartBuilder, ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 64, 32);
	}

	@Inject(method = "setAngles(Lnet/minecraft/client/render/entity/state/CreeperEntityRenderState;)V", at = @At("HEAD"), cancellable = true)
	public void setAngles(CreeperEntityRenderState creeperEntityRenderState, CallbackInfo ci) {
		ci.cancel();
	}
}
