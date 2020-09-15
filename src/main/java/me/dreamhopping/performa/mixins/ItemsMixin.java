package me.dreamhopping.performa.mixins;

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

@Mixin(Items.class)
public abstract class ItemsMixin {
    @Shadow
    private static Item register(Block block, ItemGroup group) {
        return null;
    }

    /**
     * @author Mojang / dreamhopping
     * @reason To fix MC-132820: "Spawner isn't in the creative inventory"
     */
    @Inject(method = "register(Lnet/minecraft/block/Block;)Lnet/minecraft/item/Item;", at = @At("HEAD"), cancellable = true)
    private static void register(Block block, CallbackInfoReturnable<Item> cir) {
        if (block instanceof SpawnerBlock) {
            cir.setReturnValue(register(Blocks.SPAWNER, ItemGroup.MISC));
        }
    }
}
