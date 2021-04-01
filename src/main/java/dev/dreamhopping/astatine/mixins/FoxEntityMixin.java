/*
 *     Astatine is a mod for Minecraft which fixes many common bugs in the game
 *     Copyright (C) 2021 Conor Byrne
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.dreamhopping.astatine.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Fix MC-153010: "doMobLoot" gamerule doesn't prevent foxes from dropping their items
 *
 * @author DJtheRedstoner
 */
@Mixin(FoxEntity.class)
public abstract class FoxEntityMixin extends Entity {
    /**
     * Required constructor for mixin to function
     */
    public FoxEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * Adds an additional check when dropping foxes held items
     *
     * @return false to continue dropping item, true to cancel
     */
    @Redirect(
        method = "drop",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/ItemStack;isEmpty()Z"
        )
    )
    private boolean shouldDrop(ItemStack itemStack) {
        // Do the default check we redirect
        if (itemStack.isEmpty()) return false;
        // Add an additional check for the doMobLoot gamerule
        return !world.getGameRules().getBoolean(GameRules.DO_MOB_LOOT);
    }
}
