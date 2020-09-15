package me.dreamhopping.performa;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Performa implements ModInitializer {
    public final Logger logger = LogManager.getLogger("Performa");

    @Override
    public void onInitialize() {
        logger.info("Starting Performa v" + PerformaMetadata.VERSION);
    }
}
