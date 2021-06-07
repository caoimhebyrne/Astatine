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

import net.minecraft.client.gui.widget.SliderWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fix MC-145748: Clicking a button when there's a slider under the mouse in the next screen plays the click sound twice
 *
 * @author Conor Byrne
 */
@Mixin(SliderWidget.class)
public class SliderWidgetMixin {
    private boolean sliderClicked = false;

    /**
     * Set the onClick variable to true when the slider is clicked
     * This ensures that the sound is only played when the slider is actually used
     */
    @Inject(method = "onClick", at = @At("HEAD"))
    private void onClick(double mouseX, double mouseY, CallbackInfo ci) {
        sliderClicked = true;
    }

    /**
     * Cancel the AbstractButtonWidget#playDownSound invocation if the slider was not actually clicked
     */
    @Inject(method = "onRelease", at = @At("HEAD"), cancellable = true)
    private void onRelease(double mouseX, double mouseY, CallbackInfo ci) {
        if (!sliderClicked) ci.cancel();
        else sliderClicked = false;
    }
}
