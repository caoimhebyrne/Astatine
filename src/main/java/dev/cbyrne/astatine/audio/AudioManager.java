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

package dev.cbyrne.astatine.audio;

import dev.cbyrne.astatine.mixins.accessor.SoundManagerAccessor;
import dev.cbyrne.astatine.mixins.accessor.SoundSystemAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Feature: Allow switching between audio devices from inside of Minecraft
 *
 * @author Conor Byrne
 */
public class AudioManager {
    private static final AudioManager instance = new AudioManager();
    public final Logger logger = LogManager.getLogger("Astatine: AudioManager");
    public List<String> devices = new ArrayList<>();

    public static AudioManager getInstance() {
        return instance;
    }

    /**
     * Fetches all audio devices capable for usage via OpenAL
     */
    public void fetchDevices() {
        boolean firstRun = devices.isEmpty();

        try {
            devices = ALUtil.getStringList(0, ALC11.ALC_ALL_DEVICES_SPECIFIER);
            if (firstRun) logger.info("Found " + AudioManager.getInstance().devices.size() + " device(s)");
        } catch (Throwable t) {
            logger.error("Failed to fetch OpenAL devices: ", t);
        }
    }

    /**
     * Restarts the Minecraft SoundSystem without reloading the sound assets via Mixin Accessors
     */
    public void restartSoundSystem() {
        SoundManagerAccessor soundManagerAccessor = (SoundManagerAccessor) MinecraftClient.getInstance().getSoundManager();

        SoundSystem soundSystem = soundManagerAccessor.getSoundSystem();
        SoundSystemAccessor soundSystemAccessor = (SoundSystemAccessor) soundSystem;

        soundSystem.stop();
        soundSystemAccessor.invokeStart();
    }

    public static class Configuration {
        public static String SELECTED_SOUND_DEVICE = "";
    }
}
