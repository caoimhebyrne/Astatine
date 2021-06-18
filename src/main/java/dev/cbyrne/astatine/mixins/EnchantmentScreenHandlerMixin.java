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

import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Fix MC-2474: Transparent blocks (ie: snow) placed between bookshelves and enchantment tables negate bonuses received from bookshelves
 * The fix allows a player to place *transparent* blocks between the enchantment table and the bookshelves, solid blocks (i.e. stone, wood) will still block that bookshelf
 *
 * @author Conor Byrne
 */
@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin {
    /**
     * Redirects all [World.isAir] method calls to [BlockState.isSolidBlock]
     * Note: method_17411 is the lambda inside EnchantmentScreenHandler#onContentChanged (line 93) which checks if the neighboring blocks are air
     *
     * @param world The world the player is currently in
     * @param pos   The position of the block being checked
     * @return If the block is a transparent block, return true, otherwise false
     */
    @Redirect(
        method = "method_17411",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;isAir(Lnet/minecraft/util/math/BlockPos;)Z"
        )
    )
    private boolean redirectIsAir(World world, BlockPos pos) {
        // Check if the block is a solid block & invert the return value
        return !world.getBlockState(pos).isSolidBlock(world, pos);
    }
}
