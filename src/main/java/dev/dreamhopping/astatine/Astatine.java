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

package dev.dreamhopping.astatine;

import dev.dreamhopping.astatine.config.AstatineConfig;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main class for Astatine, only used for logging and other minor things
 *
 * @author Conor Byrne (dreamhopping)
 */
public class Astatine implements ModInitializer {
    public final Logger logger = LogManager.getLogger("Astatine");
    public final AstatineConfig config = new AstatineConfig();

    @Override
    public void onInitialize() {
        logger.info("Astatine v" + AstatineMetadata.VERSION + " has been loaded successfully!");
        config.load();
    }
}
