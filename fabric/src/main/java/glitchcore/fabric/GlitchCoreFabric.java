/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric;

import glitchcore.event.EventManager;
import glitchcore.event.RegistryEvent;
import glitchcore.event.client.ItemTooltipEvent;
import glitchcore.event.client.RegisterColorsEvent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Comparator;

public class GlitchCoreFabric implements ModInitializer
{
    @Override
    public void onInitialize()
    {
        ClientLifecycleEvents.CLIENT_STARTED.register(client -> {
            // Fire color registration events
            EventManager.fire(new RegisterColorsEvent.Block(ColorProviderRegistry.BLOCK::register));
            EventManager.fire(new RegisterColorsEvent.Item(ColorProviderRegistry.ITEM::register));
        });

        ItemTooltipCallback.EVENT.register((stack, context, lines) -> {
            EventManager.fire(new ItemTooltipEvent(stack, lines));
        });
    }

    public static void prepareEvents()
    {
        postRegisterEvents();
    }

    private static void postRegisterEvents()
    {
        // We use LOADERS to ensure objects are registered at the correct time relative to each other
        for (ResourceLocation registryName : BuiltInRegistries.LOADERS.keySet())
        {
            ResourceKey<? extends Registry<?>> registryKey = ResourceKey.createRegistryKey(registryName);
            Registry<?> registry = BuiltInRegistries.REGISTRY.get(registryName);
            EventManager.fire(new RegistryEvent(registryKey, (location, value) -> Registry.register((Registry<? super Object>)registry, location, value)));
        }
    }
}
