package com.wildbound.core.xp;

import com.wildbound.core.progression.ProgressionManager;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * Conecta las fuentes de XP configurables (matar mobs, romper bloques) con
 * {@link ProgressionManager}.
 *
 * [PENDIENTE DE PRUEBA] Esta clase usa varios nombres de clase de Minecraft
 * (no de Fabric API) bajo el supuesto de que siguen la nomenclatura oficial
 * de Mojang en 26.3: `BuiltInRegistries`, `LivingEntity`, `Entity`. No se ha
 * compilado en este entorno; si algún import falla, es el primer sitio a
 * revisar (ver ROADMAP.md → Decisiones, cambio de mapeos en 26.1).
 */
public final class XpHooks {

	private XpHooks() {
	}

	public static void register() {
		ServerLivingEntityEvents.AFTER_DEATH.register((entity, damageSource) -> {
			Entity attacker = damageSource.getEntity();
			if (!(attacker instanceof ServerPlayer player)) {
				return;
			}
			if (!(entity instanceof LivingEntity)) {
				return;
			}
			String entityTypeId = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
			long xp = XpSourcesConfig.xpForMobKill(entityTypeId);
			if (xp > 0) {
				ProgressionManager.INSTANCE.addXp(player, xp);
			}
		});

		PlayerBlockBreakEvents.AFTER.register((level, player, pos, state, blockEntity) -> {
			if (!(player instanceof ServerPlayer serverPlayer)) {
				return;
			}
			String blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString();
			long xp = XpSourcesConfig.xpForBlockBreak(blockId);
			if (xp > 0) {
				ProgressionManager.INSTANCE.addXp(serverPlayer, xp);
			}
		});
	}
}
