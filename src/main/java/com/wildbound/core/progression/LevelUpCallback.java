package com.wildbound.core.progression;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

/**
 * Evento disparado cuando un jugador sube uno o más niveles.
 *
 * Pensado para que la Fase 3 (HUD) y futuras fases (desbloqueo de
 * habilidades) se enganchen aquí sin tocar ProgressionManager.
 */
public interface LevelUpCallback {

	Event<LevelUpCallback> EVENT = EventFactory.createArrayBacked(LevelUpCallback.class,
			(listeners) -> (player, oldLevel, newLevel) -> {
				for (LevelUpCallback listener : listeners) {
					listener.onLevelUp(player, oldLevel, newLevel);
				}
			});

	void onLevelUp(ServerPlayer player, int oldLevel, int newLevel);
}
