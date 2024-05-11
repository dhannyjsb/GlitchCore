/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import glitchcore.forge.renderer.IExtendedDebugRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DebugRenderer.class)
public abstract class MixinDebugRenderer implements IExtendedDebugRenderer
{
    @Unique
    private PoseStack lastPoseStack;

    @Inject(method="render", at=@At(value="HEAD"))
    public void onRender(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, double x, double y, double z, CallbackInfo ci)
    {
        this.lastPoseStack = poseStack;
    }

    @Override
    public PoseStack getLastPoseStack()
    {
        return this.lastPoseStack;
    }
}
