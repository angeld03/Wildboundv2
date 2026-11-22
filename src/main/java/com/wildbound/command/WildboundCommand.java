package com.wildbound.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.wildbound.core.progression.PlayerProgression;
import com.wildbound.core.progression.ProgressionManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Comando de depuración: /wildbound level get|set <jugador> [nivel],
 * /wildbound xp add <jugador> <cantidad>.
 *
 * [PENDIENTE DE PRUEBA] Igual que XpHooks: usa nombres oficiales de Mojang
 * (`Commands`, `EntityArgument`, `Component`) sin haber compilado en este
 * entorno. El paquete exacto de `CommandRegistrationCallback`
 * (`net.fabricmc.fabric.api.command.v2`) también debe confirmarse: en
 * versiones de Fabric API anteriores a 26.x vivía en el paquete "v2" del
 * módulo de comandos; si cambió de módulo, el IDE lo señalará al compilar.
 */
public final class WildboundCommand {

	private WildboundCommand() {
	}

	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(Commands.literal("wildbound")
					.requires(source -> source.hasPermission(2))
					.then(Commands.literal("level")
							.then(Commands.literal("get")
									.then(Commands.argument("jugador", EntityArgument.player())
											.executes(ctx -> {
												ServerPlayer target = EntityArgument.getPlayer(ctx, "jugador");
												PlayerProgression progression = ProgressionManager.INSTANCE.get(target);
												ctx.getSource().sendSuccess(() -> Component.literal(
														target.getName().getString() + " -> nivel " + progression.getLevel()
																+ " (" + progression.getXp() + "/" + progression.getXpForNextLevel() + " xp)"
												), false);
												return 1;
											})))
							.then(Commands.literal("set")
									.then(Commands.argument("jugador", EntityArgument.player())
											.then(Commands.argument("nivel", IntegerArgumentType.integer(1))
													.executes(ctx -> {
														ServerPlayer target = EntityArgument.getPlayer(ctx, "jugador");
														int nivel = IntegerArgumentType.getInteger(ctx, "nivel");
														ProgressionManager.INSTANCE.get(target).setLevel(nivel);
														ctx.getSource().sendSuccess(() -> Component.literal(
																target.getName().getString() + " ahora es nivel " + nivel
														), true);
														return 1;
													}))))
					)
					.then(Commands.literal("xp")
							.then(Commands.literal("add")
									.then(Commands.argument("jugador", EntityArgument.player())
											.then(Commands.argument("cantidad", IntegerArgumentType.integer(1))
													.executes(ctx -> {
														ServerPlayer target = EntityArgument.getPlayer(ctx, "jugador");
														int cantidad = IntegerArgumentType.getInteger(ctx, "cantidad");
														ProgressionManager.INSTANCE.addXp(target, cantidad);
														ctx.getSource().sendSuccess(() -> Component.literal(
																"+" + cantidad + " xp para " + target.getName().getString()
														), true);
														return 1;
													}))))
					)
			);
		});
	}
}
