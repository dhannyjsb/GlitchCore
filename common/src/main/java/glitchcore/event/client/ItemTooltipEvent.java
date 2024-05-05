/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.event.client;

import glitchcore.event.Event;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ItemTooltipEvent extends Event
{
    private final Player player;
    private final ItemStack stack;
    private final List<Component> tooltip;

    public ItemTooltipEvent(Player player, ItemStack stack, List<Component> tooltip)
    {
        this.player = player;
        this.stack = stack;
        this.tooltip = tooltip;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public ItemStack getStack()
    {
        return this.stack;
    }

    public List<Component> getTooltip()
    {
        return this.tooltip;
    }
}
