package dev.dreamhopping.astatine.mixins;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Fix MC-117800: Half bed can be placed outside the world's border
 *
 * @author DJtheRedstoner
 */
@Mixin(BedBlock.class)
public class BedBlockMixin {

    /**
     * Add an additional check when checking if the 2nd part of bed can be placed that adds a check if
     * its inside current world border.
     */
    @Redirect(
        method = "getPlacementState",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;canReplace(Lnet/minecraft/item/ItemPlacementContext;)Z"
        )
    )
    private boolean canReplace(BlockState blockState, ItemPlacementContext ctx) {
        // Get the position of the 2nd part of bed
        BlockPos targetPos = ctx.getBlockPos().offset(ctx.getPlayerFacing());
        // Do default check as well as world border check
        return blockState.canReplace(ctx) && ctx.getWorld().getWorldBorder().contains(targetPos);
    }

}
