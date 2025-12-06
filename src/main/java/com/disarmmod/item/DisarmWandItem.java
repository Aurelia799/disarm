package com.disarmmod.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.server.level.ServerLevel;

public class DisarmWandItem extends Item {
    private static final EquipmentSlot[] ARMOR_SLOTS = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };

    public DisarmWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, net.minecraft.world.InteractionHand hand) {
        if (!player.level().isClientSide()) {
            boolean droppedSomething = false;

            // Drop all armor pieces
            for (EquipmentSlot slot : ARMOR_SLOTS) {
                ItemStack armorStack = target.getItemBySlot(slot);
                if (!armorStack.isEmpty()) {
                    dropItem(target, armorStack.copy());
                    target.setItemSlot(slot, ItemStack.EMPTY);
                    droppedSomething = true;
                }
            }

            // Drop main hand item
            ItemStack mainHandItem = target.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!mainHandItem.isEmpty()) {
                dropItem(target, mainHandItem.copy());
                target.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                droppedSomething = true;
            }

            // Drop off hand item
            ItemStack offHandItem = target.getItemBySlot(EquipmentSlot.OFFHAND);
            if (!offHandItem.isEmpty()) {
                dropItem(target, offHandItem.copy());
                target.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
                droppedSomething = true;
            }

            if (droppedSomething) {
                // Play sound effect
                player.level().playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.ITEM_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);

                // Spawn particles
                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.CRIT,
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

    private void dropItem(LivingEntity entity, ItemStack stack) {
        ItemEntity itemEntity = new ItemEntity(entity.level(),
                entity.getX(), entity.getY() + 0.5, entity.getZ(), stack);
        itemEntity.setPickUpDelay(10);
        entity.level().addFreshEntity(itemEntity);
    }
}
