package com.disarmmod.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;

import java.util.Random;

public class EquipWandItem extends Item {
    private static final Random RANDOM = new Random();

    // Armor sets to choose from
    private static final Item[][] ARMOR_SETS = {
            {Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS},
            {Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS},
            {Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS},
            {Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS},
            {Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS},
            {Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS}
    };

    // Weapons to choose from
    private static final Item[] WEAPONS = {
            Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD,
            Items.DIAMOND_SWORD, Items.NETHERITE_SWORD,
            Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.GOLDEN_AXE,
            Items.DIAMOND_AXE, Items.NETHERITE_AXE,
            Items.BOW, Items.CROSSBOW, Items.TRIDENT
    };

    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    public EquipWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, net.minecraft.world.InteractionHand hand) {
        if (!player.level().isClientSide() && target instanceof Mob mob) {
            boolean equippedSomething = false;

            // Randomly select an armor set
            Item[] selectedArmor = ARMOR_SETS[RANDOM.nextInt(ARMOR_SETS.length)];

            // Equip armor to each slot
            for (int i = 0; i < ARMOR_SLOTS.length; i++) {
                EquipmentSlot slot = ARMOR_SLOTS[i];
                if (mob.getItemBySlot(slot).isEmpty()) {
                    ItemStack armorStack = new ItemStack(selectedArmor[i]);
                    mob.setItemSlot(slot, armorStack);
                    // Set drop chance to 100%
                    mob.setDropChance(slot, 1.0F);
                    equippedSomething = true;
                }
            }

            // Equip a random weapon in main hand
            if (mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
                Item selectedWeapon = WEAPONS[RANDOM.nextInt(WEAPONS.length)];
                ItemStack weaponStack = new ItemStack(selectedWeapon);
                mob.setItemSlot(EquipmentSlot.MAINHAND, weaponStack);
                mob.setDropChance(EquipmentSlot.MAINHAND, 1.0F);
                equippedSomething = true;
            }

            // Maybe add a shield to offhand (30% chance)
            if (mob.getItemBySlot(EquipmentSlot.OFFHAND).isEmpty() && RANDOM.nextFloat() < 0.3f) {
                mob.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
                mob.setDropChance(EquipmentSlot.OFFHAND, 1.0F);
                equippedSomething = true;
            }

            if (equippedSomething) {
                // Play sound effect
                player.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 1.0F, 1.0F);

                // Spawn particles
                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                            target.getX(), target.getY() + target.getBbHeight() / 2, target.getZ(),
                            15, 0.3, 0.5, 0.3, 0.1);
                }

                // Cooldown
                player.getCooldowns().addCooldown(this, 20);
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.sidedSuccess(player.level().isClientSide());
    }
}
