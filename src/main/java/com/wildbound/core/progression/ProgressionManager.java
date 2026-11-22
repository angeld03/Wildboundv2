package com.wildbound.core.progression;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Guarda y da acceso a la progresión (nivel/XP) de cada jugador conectado.
 *
 * Persistencia: un archivo JSON por jugador en
 * "<carpeta del juego>/wildbound/progression/<uuid>.json", en vez de usar el
 * sistema SavedData de Minecraft (cuya firma exacta en 26.3 no se ha podido
 * verificar sin compilar). Es una simplificación deliberada para la Fase 1:
 * funciona igual para un servidor dedicado o integrado, y se puede migrar a
 * SavedData más adelante sin cambiar la API pública de esta clase.
 *
 * [PENDIENTE DE PRUEBA] "ServerPlayConnectionEvents" es el nombre de Fabric
 * API para los eventos de conexión/desconexión de jugador; no estaba en la
 * lista de renombrados consultada, pero confírmalo al compilar.
 */
public final class ProgressionManager {

	public static final ProgressionManager INSTANCE = new ProgressionManager();

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final Map<UUID, PlayerProgression> progressionByPlayer = new ConcurrentHashMap<>();

	private ProgressionManager() {
	}

	public void init() {
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayer player = handler.getPlayer();
			progressionByPlayer.put(player.getUUID(), load(player.getUUID()));
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayer player = handler.getPlayer();
			PlayerProgression progression = progressionByPlayer.remove(player.getUUID());
			if (progression != null) {
				save(progression);
			}
		});
	}

	public PlayerProgression get(ServerPlayer player) {
		return progressionByPlayer.computeIfAbsent(player.getUUID(), PlayerProgression::new);
	}

	/**
	 * Añade XP a un jugador, sube de nivel si corresponde, dispara
	 * {@link LevelUpCallback} y persiste el resultado inmediatamente.
	 */
	public void addXp(ServerPlayer player, long amount) {
		PlayerProgression progression = get(player);
		int before = progression.getLevel();
		int levelsGained = progression.addXp(amount);
		if (levelsGained > 0) {
			LevelUpCallback.EVENT.invoker().onLevelUp(player, before, progression.getLevel());
		}
		save(progression);
	}

	private Path fileFor(UUID playerId) {
		return FabricLoader.getInstance().getGameDir()
				.resolve("wildbound").resolve("progression").resolve(playerId + ".json");
	}

	private PlayerProgression load(UUID playerId) {
		Path path = fileFor(playerId);
		if (!Files.exists(path)) {
			return new PlayerProgression(playerId);
		}
		try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			StoredData data = GSON.fromJson(reader, StoredData.class);
			if (data == null) {
				return new PlayerProgression(playerId);
			}
			return new PlayerProgression(playerId, data.level, data.xp, data.activeElementId);
		} catch (IOException e) {
			return new PlayerProgression(playerId);
		}
	}

	private void save(PlayerProgression progression) {
		Path path = fileFor(progression.getPlayerId());
		try {
			Files.createDirectories(path.getParent());
			StoredData data = new StoredData();
			data.level = progression.getLevel();
			data.xp = progression.getXp();
			data.activeElementId = progression.getActiveElementId();
			try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
				GSON.toJson(data, writer);
			}
		} catch (IOException e) {
			// Fallo de guardado no debe tumbar el servidor; se reintentará en la
			// siguiente llamada a addXp o al desconectar.
		}
	}

	private static final class StoredData {
		int level = 1;
		long xp = 0L;
		String activeElementId;
	}
}
