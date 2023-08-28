package com.ferri.arnus.sculkvision.network;

import com.ferri.arnus.sculkvision.EntityExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GlowPacket {

    private final int id;

    public GlowPacket(int id) {
        this.id = id;
    }

    public GlowPacket(FriendlyByteBuf buf) {
        this.id = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(id);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        Handler.handle(id);
    }

    static class Handler {
        public static void handle(int id) {
            Entity entity = Minecraft.getInstance().level.getEntity(id);
            if (entity instanceof LivingEntity living) {
                ((EntityExtension)living).setGlowing(true, 800);
            }
        }
    }


}
