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

package dev.dreamhopping.astatine.config;

import dev.dreamhopping.astatine.audio.AudioManager;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * The main configuration class for Astatine
 *
 * @author Conor Byrne (dreamhopping)
 */
public class AstatineConfig {
    private final Logger logger = LogManager.getLogger("Astatine: Configuration");
    private final File configDir = new File(FabricLoader.getInstance().getConfigDir().toFile(), "astatine");
    private final File audioManagerConfig = new File(configDir, "audiomanager.properties");

    public void load() {
        try {
            if (!configDir.exists()) configDir.mkdir();
            if (!audioManagerConfig.exists()) audioManagerConfig.createNewFile();

            // Load configuration
            Properties fileProperties = new Properties();
            try (FileInputStream fileInputStream = new FileInputStream(audioManagerConfig)) {
                fileProperties.load(fileInputStream);
            }

            AudioManager.Configuration.SELECTED_SOUND_DEVICE = fileProperties.getProperty("SELECTED_SOUND_DEVICE", "");
        } catch (Exception e) {
            logger.info("Failed to read config \"" + audioManagerConfig.getName() + "\"", e);
        }

        // Save config on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(this::save));
    }

    public void save() {
        Properties fileProperties = new Properties();
        fileProperties.setProperty("SELECTED_SOUND_DEVICE", AudioManager.Configuration.SELECTED_SOUND_DEVICE);

        try (FileOutputStream fileOutputStream = new FileOutputStream(audioManagerConfig)) {
            fileProperties.store(fileOutputStream, "Astatine Properties File for AudioManager");
        } catch (IOException e) {
            logger.info("Failed to save config \"" + audioManagerConfig.getName() + "\"", e);
        }
    }
}
