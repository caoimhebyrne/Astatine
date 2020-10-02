package me.dreamhopping.performa.mixins;

import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TraderOfferList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

/**
 * @author Mojang / dreamhopping
 * To fix MC-148998: "Items are offset in wandering trader's (and villager's) trading menu"
 */
@Mixin(MerchantScreen.class)
public class MerchantScreenMixin {
    boolean shouldOffset = false;

    @ModifyVariable(method = "render", name = {"n"}, at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Lnet/minecraft/client/gui/screen/ingame/MerchantScreen;method_20222(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;II)V"))
    public int fixOffset(int n) {
        return shouldOffset ? n + 1 : n;
    }

    @Inject(method = "render", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "INVOKE"))
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci, TraderOfferList traderOfferList, int i, int j, int k, int l, int m, Iterator var11, TradeOffer tradeOffer, ItemStack itemStack, ItemStack itemStack2, ItemStack itemStack3, ItemStack itemStack4, int n) {
        shouldOffset = !(itemStack.getItem() instanceof BlockItem);
    }
}
