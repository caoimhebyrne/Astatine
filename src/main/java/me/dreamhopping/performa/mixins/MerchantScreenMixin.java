package me.dreamhopping.performa.mixins;

import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MerchantScreen.class)
public class MerchantScreenMixin {
    /**
     * @author Mojang / dreamhopping
     * @reason To fix MC-148998: "Items are offset in wandering trader's (and villager's) trading menu"
     */
    @ModifyVariable(method = "render", name = {"n"}, at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/gui/screen/ingame/MerchantScreen;method_20222(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;II)V"))
    public int fixOffset(int n) {
        // TODO: If the itemstack in render is an item, don't change the variable
        return n + 1;
    }
}
