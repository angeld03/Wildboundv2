package com.wildbound.client;

import com.wildbound.Wildbound;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Punto de entrada de cliente. Registra la tecla H para abrir/cerrar la HUD
 * detallada (la pantalla en sí se construye en la Fase 3, ver ROADMAP.md).
 */
public class WildboundClient implements ClientModInitializer {

	private static KeyBinding openHudKey;

	@Override
	public void onInitializeClient() {
		openHudKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.wildbound.open_hud",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_H,
				"category.wildbound.general"
		));

		net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openHudKey.wasPressed()) {
				onOpenHudPressed(client);
			}
		});

		Wildbound.LOGGER.info("[Wildbound] Cliente inicializado, tecla H registrada (fase 0)");
	}

	private void onOpenHudPressed(MinecraftClient client) {
		// TODO (Fase 3): abrir la pantalla de HUD detallada en lugar de este log.
		Wildbound.LOGGER.info("[Wildbound] Tecla H pulsada: aqui se abrira la HUD (pendiente fase 3)");
	}
}
