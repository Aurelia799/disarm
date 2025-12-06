package com.disarmmod;

import com.disarmmod.item.DisarmWandItem;
import com.disarmmod.item.EquipWandItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, DisarmMod.MOD_ID);

    public static final RegistryObject<Item> DISARM_WAND = ITEMS.register("disarm_wand",
            () -> new DisarmWandItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> EQUIP_WAND = ITEMS.register("equip_wand",
            () -> new EquipWandItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
