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

import net.minecraft.block.BlockState;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static net.minecraft.block.SeaPickleBlock.isDry;

/**
 * Fix MC-127995: "You can use bone meal on sea pickles in situations where no sea pickles will grow"
 *
 * @author Conor Byrne (dreamhopping)
 */
@Mixin(SeaPickleBlock.class)
public class SeaPickleBlockMixin {
    /**
     * Specify certain conditions for isFertilizable instead of always returning true
     *
     * @return If the sea pickle is underwater and the block below it is a coral block return true, otherwise false
     * @author Conor Byrne (dreamhopping)
     */
    @Overwrite
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        // Check if the sea pickle is underwater and on-top of a coral block
        return !isDry(state) && world.getBlockState(pos.down()).isIn(BlockTags.CORAL_BLOCKS);
    }
}
