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

import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOfferList;
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
    boolean isBlock = false;

    /**
     * Overrides the "n" variable to adjust the y position of the item being rendered
     *
     * @param n The y position of the item being rendered
     * @return If shouldOffset is true, the original offset will have one added to it, otherwise do nothing
     */
    @ModifyVariable(method = "render", ordinal = 7, at = @At("STORE"))
    public int fixOffset(int n) {
        return isBlock ? n + 1 : n;
    }

    /**
     * Sets isBlock to true if the item being rendered is a "Block Item"
     * This is needed as blocks are rendered one pixel too high, causing them to look weird in the trading GUI
     *
     * @param itemStack The item that is being rendered
     */
    @Inject(method = "render", locals = LocalCapture.CAPTURE_FAILSOFT, at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/village/TradeOffer;getMutableSellItem()Lnet/minecraft/item/ItemStack;"))
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci, TradeOfferList tradeOfferList, int i, int j, int k, int l, int m, Iterator var11, TradeOffer tradeOffer, ItemStack itemStack, ItemStack itemStack2, ItemStack itemStack3, ItemStack itemStack4) {
        isBlock = !(itemStack.getItem() instanceof BlockItem);
    }
}
