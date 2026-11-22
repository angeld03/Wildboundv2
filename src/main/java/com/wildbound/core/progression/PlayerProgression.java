package com.wildbound.core.progression;

import java.util.UUID;

/**
 * Datos de progresión de un jugador: nivel, experiencia y elemento activo.
 *
 * Clase intencionadamente sin ninguna dependencia de las clases de Minecraft,
 * para que la lógica de niveles sea fácil de probar y no dependa de nombres
 * de API que puedan cambiar entre versiones de Fabric.
 */
public class PlayerProgression {

	private final UUID playerId;
	private int level;
	private long xp;
	private String activeElementId;

	public PlayerProgression(UUID playerId) {
		this(playerId, 1, 0L, null);
	}

	public PlayerProgression(UUID playerId, int level, long xp, String activeElementId) {
		this.playerId = playerId;
		this.level = Math.max(1, level);
		this.xp = Math.max(0, xp);
		this.activeElementId = activeElementId;
	}

	public UUID getPlayerId() {
		return playerId;
	}

	public int getLevel() {
		return level;
	}

	public long getXp() {
		return xp;
	}

	public String getActiveElementId() {
		return activeElementId;
	}

	public void setActiveElementId(String activeElementId) {
		this.activeElementId = activeElementId;
	}

	public void setLevel(int level) {
		this.level = Math.max(1, level);
	}

	public void setXp(long xp) {
		this.xp = Math.max(0, xp);
	}

	/** XP total acumulada necesaria para alcanzar el nivel dado (curva cuadrática simple). */
	public static long totalXpForLevel(int level) {
		if (level <= 1) {
			return 0L;
		}
		long n = level - 1L;
		// 50 * n * (n + 1): nivel 2 = 100 xp, nivel 3 = 300 xp, nivel 10 = 4500 xp...
		// Ajustable según el testeo del usuario; centralizado aquí a propósito.
		return 50L * n * (n + 1L);
	}

	public long getXpForNextLevel() {
		return totalXpForLevel(level + 1);
	}

	/**
	 * Añade experiencia y sube de nivel tantas veces como corresponda.
	 *
	 * @return el número de niveles subidos (0 si no sube ninguno).
	 */
	public int addXp(long amount) {
		if (amount <= 0) {
			return 0;
		}
		this.xp += amount;
		int levelsGained = 0;
		while (this.xp >= getXpForNextLevel()) {
			this.level++;
			levelsGained++;
		}
		return levelsGained;
	}
}
