package dev.dreamhopping.performa.mixins;

import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Fix MC-2474: Transparent blocks (ie: snow) placed between bookshelves and enchantment tables negate bonuses received from bookshelves
 * The fix allows a player to place *transparent* blocks between the enchantment table and the bookshelves, solid blocks (i.e. stone, wood) will still block that bookshelf
 */
@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin {
    /**
     * Redirects all [World.isAir] method calls to [BlockState.isSolidBlock]
     * Note: method_17411 is the lambda which the isAir method is called from (ScreenHandlerContext#run, l94)
     *
     * @param world The world the player is currently in
     * @param pos   The position of the block being checked
     * @return If the block is a transparent block, return true, otherwise false
     */
    @Redirect(method = "method_17411", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isAir(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean isAir(World world, BlockPos pos) {
        // Check if the block is a solid block & invert the return value
        return !world.getBlockState(pos).isSolidBlock(world, pos);
    }
}
