package net.scade.titlecard;

import net.fabricmc.api.ModInitializer;

import net.scade.titlecard.config.TitleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TitleCard implements ModInitializer {
	public static final String MOD_ID = "titlecard";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



	@Override
	public void onInitialize() {
        LOGGER.info("Mark I made a steak!");
        LOGGER.info("A steak?!");

        TitleConfig.load();
	}
}