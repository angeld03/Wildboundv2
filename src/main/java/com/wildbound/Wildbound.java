package com.wildbound;

import com.wildbound.core.progression.ProgressionManager;
import com.wildbound.core.xp.XpHooks;
import com.wildbound.core.xp.XpSourcesConfig;
import com.wildbound.command.WildboundCommand;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Punto de entrada principal (cliente + servidor) del mod Wildbound.
 */
public class Wildbound implements ModInitializer {

	public static final String MOD_ID = "wildbound";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/** Atajo para crear un ResourceLocation con el namespace de este mod. */
	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		LOGGER.info("[Wildbound] Inicializando mod (fase 1 - nucleo RPG)");

		XpSourcesConfig.load();
		ProgressionManager.INSTANCE.init();
		XpHooks.register();
		WildboundCommand.register();

		// TODO (Fase 2): registrar elementos y habilidades desde datos JSON.
	}
}
