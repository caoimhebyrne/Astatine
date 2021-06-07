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

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fix MC-75721: Arrow buttons in book UI are rendered in front of tooltips
 *
 * @author Conor Byrne
 */
@Mixin(BookScreen.class)
public abstract class BookScreenMixin extends Screen {
    /**
     * Required constructor for extending [Screen] class, if we don't do this there will be compiler errors
     */
    protected BookScreenMixin() {
        super(null);
    }

    @Shadow
    @Nullable
    public abstract Style getTextAt(double x, double y);

    /**
     * Calls the [Screen.render] function before running [renderTextHoverEffect], if we don't do this then the arrow buttons will not render correctly
     */
    @Inject(method = "render", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/BookScreen;renderTextHoverEffect(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Style;II)V"))
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        // Call the original render function
        super.render(matrices, mouseX, mouseY, delta);

        // Render the hover text
        Style style = getTextAt(mouseX, mouseY);
        renderTextHoverEffect(matrices, style, mouseX, mouseY);

        // Return here, the rest of the method doesn't need to be executed
        ci.cancel();
    }
}
