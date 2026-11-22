package com.wildbound;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Punto de entrada principal (cliente + servidor) del mod Wildbound.
 *
 * Fase 0 (cimientos): solo confirma que el mod carga. La lógica de niveles,
 * elementos y habilidades se añade en las fases 1 y 2 (ver ROADMAP.md).
 */
public class Wildbound implements ModInitializer {

	public static final String MOD_ID = "wildbound";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("[Wildbound] Inicializando mod (fase 0 - cimientos)");

		// TODO (Fase 1): registrar el componente de datos del jugador (nivel, XP).
		// TODO (Fase 2): registrar elementos y habilidades desde datos JSON.
	}
}
