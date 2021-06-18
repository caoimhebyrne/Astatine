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

import dev.cbyrne.astatine.gui.button.CloseButton;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Fix MC-117977: Advancement GUI doesn't have a close button
 *
 * @author Conor Byrne
 */
@Mixin(AdvancementsScreen.class)
public class AdvancementsScreenMixin extends Screen {
    /**
     * Required constructor for extending [Screen] class, if we don't do this there will be compiler errors
     */
    protected AdvancementsScreenMixin() {
        super(null);
    }

    /**
     * Add the close button to the GUI
     */
    @Inject(method = "init()V", at = @At("RETURN"))
    private void init(CallbackInfo callbackInfo) {
        // Add a button at the top right corner of the selected tab
        addDrawableChild(new CloseButton((this.width - (this.width - 252) / 2) - 20, ((this.height - 140) / 2) + 5, (ButtonWidget) -> onClose()));
    }

    /**
     * The original render call in [AdvancementsScreen.render] does not call the super method, meaning buttons are not rendered.
     * We must call the super method ourselves so the user can see the button
     */
    @Inject(method = "render", at = @At("RETURN"))
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        super.render(matrices, mouseX, mouseY, delta);
    }
}
