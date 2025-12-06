package com.disarmmod;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DisarmMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> DISARM_TAB = CREATIVE_MODE_TABS.register("disarm_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(ModItems.DISARM_WAND.get()))
                    .title(Component.translatable("creativetab.disarmmod.disarm_tab"))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.DISARM_WAND.get());
                        output.accept(ModItems.EQUIP_WAND.get());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
