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

package dev.dreamhopping.astatine.audio;

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

    public void fetchDevices() {
        try {
            logger.info("Fetching available OpenAL devices");
            devices = ALUtil.getStringList(0, ALC11.ALC_ALL_DEVICES_SPECIFIER);
            logger.info("Found " + AudioManager.getInstance().devices.size() + " device(s)");
        } catch (Throwable t) {
            logger.error("Failed to fetch OpenAL devices: ", t);
        }
    }

    public static class Configuration {
        public static String SELECTED_SOUND_DEVICE = "THIS SOUND DEVICE DOES NOT EXIST";
    }
}
