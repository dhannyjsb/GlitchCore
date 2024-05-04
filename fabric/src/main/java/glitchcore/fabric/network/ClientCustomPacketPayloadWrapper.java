/*******************************************************************************
 * Copyright 2023, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package glitchcore.fabric.network;

import glitchcore.network.CustomPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientConfigurationNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class ClientCustomPacketPayloadWrapper<T extends CustomPacket<T>> extends CustomPacketPayloadWrapper<T>
{
    public ClientCustomPacketPayloadWrapper(ResourceLocation channel, CustomPacket<T> packet)
    {
        super(channel, packet);

        if (packet.getPhase() == CustomPacket.Phase.PLAY)
        {
            PayloadTypeRegistry.playS2C().register(this.payloadType, CustomPacketPayload.codec(Impl::write, Impl::new));
            ClientPlayNetworking.registerGlobalReceiver(this.payloadType, new ClientPlayNetworking.PlayPayloadHandler() {
                @Override
                public void receive(CustomPacketPayload packet, ClientPlayNetworking.Context context) {
                    ClientCustomPacketPayloadWrapper.this.packet.handle(((Impl) packet).data, new CustomPacket.Context() {
                        @Override
                        public boolean isClientSide() {
                            return true;
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
            PayloadTypeRegistry.configurationS2C().register(this.payloadType, CustomPacketPayload.codec(Impl::write, Impl::new));
            ClientConfigurationNetworking.registerGlobalReceiver(this.payloadType, new ClientConfigurationNetworking.ConfigurationPayloadHandler() {
                @Override
                public void receive(CustomPacketPayload payload, ClientConfigurationNetworking.Context context) {
                    ClientCustomPacketPayloadWrapper.this.packet.handle(((Impl) packet).data, new CustomPacket.Context() {
                        @Override
                        public boolean isClientSide() {
                            return true;
                        }

                        @Override
                        public Optional<Player> getPlayer() {
                            return Optional.empty();
                        }
                    });
                }
            });
        }
    }
}
