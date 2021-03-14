package dev.dreamhopping.astatine.mixins;

import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BedBlock.class)
public class BedBlockMixin {

    @Redirect(
        method = "getPlacementState",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/BlockState;canReplace(Lnet/minecraft/item/ItemPlacementContext;)Z"
        )
    )
    private boolean canReplace(BlockState blockState, ItemPlacementContext ctx) {
        BlockPos targetPos = ctx.getBlockPos().offset(ctx.getPlayerFacing());
        return blockState.canReplace(ctx) && ctx.getWorld().getWorldBorder().contains(targetPos);
    }

}
