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

package dev.dreamhopping.astatine.mixins;

import dev.dreamhopping.astatine.audio.AudioManager;
import dev.dreamhopping.astatine.gui.button.AudioSwitcherButton;
import net.minecraft.client.gui.screen.options.GameOptionsScreen;
import net.minecraft.client.gui.screen.options.SoundOptionsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Feature: Allow switching between audio devices from inside of Minecraft
 *
 * @author Conor Byrne (dreamhopping)
 */
@Mixin(SoundOptionsScreen.class)
public abstract class SoundOptionsScreenMixin extends GameOptionsScreen {
    /**
     * Required to avoid compile errors when extending GameOptionsScreen
     */
    public SoundOptionsScreenMixin() {
        super(null, null, null);
    }

    /**
     * Adds an audio device button into the audio switcher screen
     */
    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        AudioManager.getInstance().fetchDevices();
        this.addButton(new AudioSwitcherButton(this.width / 2 - 155 + 160, this.height / 6 - 12 + 24 * (11 >> 1), 150, 20));
    }
}
