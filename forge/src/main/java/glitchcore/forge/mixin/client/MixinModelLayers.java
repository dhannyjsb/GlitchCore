/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.forge.mixin.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelLayers.class)
public class MixinModelLayers
{
    // TODO: These mixins are a workaround for a missed Forge patch in 1.20.6. They should ideally be removed in the future.
    @Inject(method="createSignModelName", at=@At(value="HEAD"), cancellable = true)
    private static void createSignModelName(WoodType woodType, CallbackInfoReturnable<ModelLayerLocation> cir)
    {
        ResourceLocation location = new ResourceLocation(woodType.name());
        cir.setReturnValue(new ModelLayerLocation(new ResourceLocation(location.getNamespace(), "sign/" + location.getPath()), "main"));
    }

    @Inject(method="createHangingSignModelName", at=@At(value="HEAD"), cancellable = true)
    private static void createHangingSignModelName(WoodType woodType, CallbackInfoReturnable<ModelLayerLocation> cir)
    {
        ResourceLocation location = new ResourceLocation(woodType.name());
        cir.setReturnValue(new ModelLayerLocation(new ResourceLocation(location.getNamespace(), "hanging_sign/" + location.getPath()), "main"));
    }
}
