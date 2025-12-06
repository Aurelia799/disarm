package com.disarmmod;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(DisarmMod.MOD_ID)
public class DisarmMod {
    public static final String MOD_ID = "disarmmod";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DisarmMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }
}
