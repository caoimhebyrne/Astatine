package me.dreamhopping.performa.mixins;

import net.minecraft.block.BlockState;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static net.minecraft.block.SeaPickleBlock.isDry;

@Mixin(SeaPickleBlock.class)
public class SeaPickleBlockMixin {
    /**
     * @reason To fix MC-127995: "You can use bone meal on sea pickles in situations where no sea pickles will grow"
     * @author dreamhopping / Mojang
     */
    @Overwrite
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return !isDry(state) && world.getBlockState(pos.down()).isIn(BlockTags.CORAL_BLOCKS);
    }
}
