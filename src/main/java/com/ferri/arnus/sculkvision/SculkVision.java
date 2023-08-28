package com.ferri.arnus.sculkvision;

import com.ferri.arnus.sculkvision.enchantment.EnchantmentRegistry;
import com.ferri.arnus.sculkvision.network.SculkVisionNetwork;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SculkVision.MODID)
public class SculkVision
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "sculkvision";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace

    public SculkVision() {
        EnchantmentRegistry.register();
        SculkVisionNetwork.register();
    }
}
