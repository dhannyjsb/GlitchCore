/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.network;

import glitchcore.network.CustomPacket;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class CustomPacketPayloadWrapper<T extends CustomPacket<T>>
{
    protected final ResourceLocation channel;
    protected final CustomPacket<T> packet;
    protected final CustomPacketPayload.Type<Impl> payloadType;

    public CustomPacketPayloadWrapper(ResourceLocation channel, CustomPacket<T> packet)
    {
        this.channel = channel;
        this.packet = packet;
        this.payloadType = CustomPacketPayload.createType(channel.toString());

        if (packet.getPhase() == CustomPacket.Phase.PLAY)
        {
            PayloadTypeRegistry.playC2S().register(this.payloadType, CustomPacketPayload.codec(Impl::write, Impl::new));
            ServerPlayNetworking.registerGlobalReceiver(this.payloadType, new ServerPlayNetworking.PlayPayloadHandler() {
                @Override
                public void receive(CustomPacketPayload payload, ServerPlayNetworking.Context context)
                {
                    CustomPacketPayloadWrapper.this.packet.handle(((Impl) packet).data, new CustomPacket.Context() {
                        @Override
                        public boolean isClientSide() {
                            return false;
                        }

                        @Override
                        public Optional<Player> getPlayer() {
                            return Optional.of(context.player());
                        }
                    });
                }
            });
        }
        else if (packet.getPhase() == CustomPacket.Phase.CONFIGURATION)
        {
            PayloadTypeRegistry.configurationC2S().register(this.payloadType, CustomPacketPayload.codec(Impl::write, Impl::new));
            ServerConfigurationNetworking.registerGlobalReceiver(this.payloadType, new ServerConfigurationNetworking.ConfigurationPacketHandler()
            {
                @Override
                public void receive(CustomPacketPayload payload, ServerConfigurationNetworking.Context context)
                {
                    CustomPacketPayloadWrapper.this.packet.handle(((Impl)packet).data, new CustomPacket.Context()
                    {
                        @Override
                        public boolean isClientSide()
                        {
                            return false;
                        }

                        @Override
                        public Optional<Player> getPlayer()
                        {
                            return Optional.empty();
                        }
                    });
                }
            });
        }
    }

    public CustomPacketPayload createPacket(T data)
    {
        return new Impl(data);
    }

    protected class Impl implements CustomPacketPayload
    {
        protected final T data;

        private Impl(T data)
        {
            this.data = data;
        }

        Impl(FriendlyByteBuf buf)
        {
            this.data = CustomPacketPayloadWrapper.this.packet.decode(buf);
        }

        public void write(FriendlyByteBuf buf)
        {
            this.data.encode(buf);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return CustomPacketPayloadWrapper.this.payloadType;
        }
    }
}
