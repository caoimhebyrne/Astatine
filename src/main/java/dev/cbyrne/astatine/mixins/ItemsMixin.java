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

package dev.cbyrne.astatine.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SpawnerBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.util.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Fix MC-132820: "Spawner isn't in the creative inventory"
 *
 * @author Conor Byrne
 */
@Mixin(Items.class)
public abstract class ItemsMixin {
    @Shadow
    protected static Item register(Block block, Item item) {
        throw new AssertionError("Reached the body of a shadowed method. dev.cbyrne.astatine.mixins.ItemsMixin#register l43");
    }

    /**
     * Sets the miscellaneous category for a SpawnerBlock so it will appear in the creative inventory
     */
    @Inject(
        method = "register(Lnet/minecraft/item/BlockItem;)Lnet/minecraft/item/Item;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void registerSpawnerInCreativeInventory(BlockItem item, CallbackInfoReturnable<Item> cir) {
        if (item.getBlock() instanceof SpawnerBlock)
            cir.setReturnValue(register(Blocks.SPAWNER, new BlockItem(Blocks.SPAWNER, (new Item.Settings()).group(ItemGroup.MISC).rarity(Rarity.EPIC))));
    }
}
