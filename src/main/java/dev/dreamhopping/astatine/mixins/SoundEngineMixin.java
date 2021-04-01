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
import net.minecraft.client.sound.SoundEngine;
import org.lwjgl.openal.ALC10;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Feature: Allow switching between audio devices from inside of Minecraft
 *
 * @author Conor Byrne (dreamhopping)
 */
@Mixin(SoundEngine.class)
public class SoundEngineMixin {
    /**
     * Overwrites any SoundEngineMixin#openDevice calls to allow the user to select which sound device they want to use
     */
    @Overwrite
    private static long openDevice() {
        try {
            List<String> devices = AudioManager.getInstance().devices;

            // openDevice can be called multiple times throughout the lifecycle of Minecraft
            // We don't want to check for new devices each time it is called, so only check if the devices list is empty
            if (devices.isEmpty()) {
                AudioManager.getInstance().fetchDevices();
                devices = AudioManager.getInstance().devices;
            }

            String selectedSoundDevice = AudioManager.Configuration.SELECTED_SOUND_DEVICE;
            if (!devices.contains(selectedSoundDevice)) {
                AudioManager.getInstance().logger.info("Sound device \"{}\" is not available, using system default", selectedSoundDevice);
                AudioManager.Configuration.SELECTED_SOUND_DEVICE = devices.get(0);

                return useSystemSoundDevice();
            }

            AudioManager.getInstance().logger.info("Switching to sound device \"{}\"", selectedSoundDevice);
            return ALC10.alcOpenDevice(selectedSoundDevice);
        } catch (Throwable t) {
            return useSystemSoundDevice();
        }
    }

    /**
     * Passes a null string to alcOpenDevice to use the system default
     *
     * @return the sound device's handle if successful, 0 if an error has occurred
     */
    private static long useSystemSoundDevice() {
        long deviceHandle = ALC10.alcOpenDevice((ByteBuffer) null);
        if (deviceHandle != 0L && ALC10.alcGetError(deviceHandle) == 0) {
            return deviceHandle;
        } else {
            return 0;
        }
    }
}
