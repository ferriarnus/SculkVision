package com.ferri.arnus.sculkvision.network;

import com.ferri.arnus.sculkvision.SculkVision;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class SculkVisionNetwork {

    private static final String PROTOCOL_VERSION = "1";
    private static int id;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(SculkVision.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        INSTANCE.messageBuilder(GlowPacket.class, getId()).encoder(GlowPacket::encode).decoder(GlowPacket::new).consumerMainThread(GlowPacket::handle).add();
    }

    public static int getId() {
        return id++;
    }
}
