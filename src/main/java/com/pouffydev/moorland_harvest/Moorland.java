package com.pouffydev.moorland_harvest;

import com.pouffydev.moorland_harvest.registry.MoorlandBlocks;
import com.pouffydev.moorland_harvest.registry.MoorlandEntities;
import com.pouffydev.moorland_harvest.registry.MoorlandItems;
import net.minecraft.util.Identifier;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Moorland implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod name as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("Moorland Harvest");
 	public static final String ID = "moorland_harvest";
	@Override
	public void onInitialize(ModContainer mod) {
		MoorlandBlocks.onInitializeBlocks();
		MoorlandBlocks.onInitializeBlockItems();
		MoorlandEntities.onInitializeEntities();
		MoorlandItems.onInitializeItems();
		LOGGER.info("Hello Quilt world from {}!", mod.metadata().name());
	}
	public static Identifier asResource(String path) {
		return new Identifier(ID, path);
	}
}
