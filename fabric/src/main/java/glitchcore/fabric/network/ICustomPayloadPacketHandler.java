package glitchcore.fabric.network;

import glitchcore.network.CustomPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public interface ICustomPayloadPacketHandler
{
    default CustomPacketPayloadWrapper<?> createPacketWrapper(ResourceLocation channel, CustomPacket<?> packet)
    {
        return new CustomPacketPayloadWrapper<>(channel, packet);
    }

    <T extends CustomPacket<T>> CustomPacketPayload createCustomPacketPayload(T packet);
}
