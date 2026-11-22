package com.wildbound.core.xp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wildbound.Wildbound;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Carga "config/wildbound/xp_sources.json": cuánta XP da matar cada tipo de
 * mob y romper cada tipo de bloque. Si el archivo no existe se crea con unos
 * valores de ejemplo razonables.
 *
 * Pensado para añadir más fuentes (misiones, jefes, logros...) sin tocar
 * código: basta con añadir un nuevo mapa aquí y un hook en XpHooks/el sistema
 * correspondiente (ver ROADMAP.md, decisión sobre fuentes de XP).
 */
public final class XpSourcesConfig {

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static Data data = defaultData();

	private XpSourcesConfig() {
	}

	public static void load() {
		Path path = configPath();
		if (!Files.exists(path)) {
			writeDefault(path);
			return;
		}
		try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
			Data loaded = GSON.fromJson(reader, Data.class);
			data = (loaded != null) ? loaded : defaultData();
		} catch (IOException e) {
			Wildbound.LOGGER.warn("[Wildbound] No se pudo leer xp_sources.json, usando valores por defecto", e);
			data = defaultData();
		}
	}

	/** XP por matar un mob, identificado por su id de registro (p. ej. "minecraft:zombie"). */
	public static long xpForMobKill(String entityTypeId) {
		return data.mobKillXp.getOrDefault(entityTypeId, 0L);
	}

	/** XP por romper un bloque, identificado por su id de registro (p. ej. "minecraft:iron_ore"). */
	public static long xpForBlockBreak(String blockId) {
		return data.blockBreakXp.getOrDefault(blockId, 0L);
	}

	private static Path configPath() {
		return FabricLoader.getInstance().getConfigDir().resolve("wildbound").resolve("xp_sources.json");
	}

	private static void writeDefault(Path path) {
		try {
			Files.createDirectories(path.getParent());
			try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
				GSON.toJson(defaultData(), writer);
			}
		} catch (IOException e) {
			Wildbound.LOGGER.warn("[Wildbound] No se pudo escribir xp_sources.json por defecto", e);
		}
	}

	private static Data defaultData() {
		Data d = new Data();
		d.mobKillXp.put("minecraft:zombie", 5L);
		d.mobKillXp.put("minecraft:skeleton", 5L);
		d.mobKillXp.put("minecraft:spider", 5L);
		d.mobKillXp.put("minecraft:creeper", 8L);
		d.mobKillXp.put("minecraft:enderman", 15L);
		d.mobKillXp.put("minecraft:ender_dragon", 500L);
		d.mobKillXp.put("minecraft:wither", 400L);

		d.blockBreakXp.put("minecraft:coal_ore", 1L);
		d.blockBreakXp.put("minecraft:iron_ore", 2L);
		d.blockBreakXp.put("minecraft:gold_ore", 3L);
		d.blockBreakXp.put("minecraft:diamond_ore", 10L);
		d.blockBreakXp.put("minecraft:emerald_ore", 10L);
		d.blockBreakXp.put("minecraft:ancient_debris", 20L);
		return d;
	}

	private static final class Data {
		Map<String, Long> mobKillXp = new LinkedHashMap<>();
		Map<String, Long> blockBreakXp = new LinkedHashMap<>();
	}
}
