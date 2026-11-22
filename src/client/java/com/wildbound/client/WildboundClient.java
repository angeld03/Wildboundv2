package com.wildbound.client;

import com.wildbound.Wildbound;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;

/**
 * Punto de entrada de cliente. Registra la tecla H para abrir/cerrar la HUD
 * detallada (la pantalla en sí se construye en la Fase 3, ver ROADMAP.md).
 *
 * NOTA sobre nombres de clase: Minecraft 26.x ya no usa mapeos Yarn, se
 * distribuye con los nombres oficiales de Mojang (ver ROADMAP.md →
 * Decisiones). Este archivo usa `KeyMapping`/`InputConstants`/`KeyMappingHelper`
 * en vez de los antiguos `KeyBinding`/`InputUtil`/`KeyBindingHelper`. No se ha
 * compilado en este entorno: si el paquete exacto de `InputConstants` o
 * `KeyMapping` no coincide al compilar, es el primer sitio a corregir.
 */
public class WildboundClient implements ClientModInitializer {

	private static final KeyMapping.Category CATEGORY =
			KeyMapping.Category.register(Wildbound.id("general"));

	private static KeyMapping openHudKey;

	@Override
	public void onInitializeClient() {
		openHudKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.wildbound.open_hud",
				InputConstants.Type.KEYSYM,
				InputConstants.KEY_H,
				CATEGORY
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openHudKey.consumeClick()) {
				// TODO (Fase 3): abrir la pantalla de HUD detallada en lugar de este log.
				Wildbound.LOGGER.info("[Wildbound] Tecla H pulsada: aqui se abrira la HUD (pendiente fase 3)");
			}
		});

		Wildbound.LOGGER.info("[Wildbound] Cliente inicializado, tecla H registrada (fase 0)");
	}
}
