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
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.dreamhopping.astatine.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fix MC-132820: "Spawner isn't in the creative inventory"
 *
 * @author Conor Byrne (dreamhopping)
 */
@Mixin(Items.class)
public abstract class ItemsMixin {
    @Shadow
    private static Item register(Block block, ItemGroup group) {
        return null;
    }

    /**
     * Applies the ItemGroup.MISC value to register for a SpawnerBlock so it will appear in the creative inventory
     */
    @Inject(method = "register(Lnet/minecraft/block/Block;)Lnet/minecraft/item/Item;", at = @At("HEAD"), cancellable = true)
    private static void register(Block block, CallbackInfoReturnable<Item> cir) {
        if (block instanceof SpawnerBlock) {
            // Register the spawner under the miscellaneous category
            cir.setReturnValue(register(Blocks.SPAWNER, ItemGroup.MISC));
        }
    }
}
