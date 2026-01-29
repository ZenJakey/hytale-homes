package com.trinex.homes.commands.home

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.protocol.GameMode
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncPlayerCommand
import com.hypixel.hytale.server.core.modules.entity.teleport.Teleport
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.Universe
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.trinex.homes.Homes
import java.util.UUID
import java.util.concurrent.CompletableFuture

class TeleHome(
    val plugin: Homes,
) : AbstractAsyncPlayerCommand("home", "Teleports you to your home. Usage: /home [home name]") {
    private val homeNameArg: RequiredArg<String> =
        this.withRequiredArg(
            "name",
            "The name of the home to teleport to.",
            ArgTypes.STRING,
        )

    init {
        setPermissionGroup(GameMode.Adventure)
    }

    override fun executeAsync(
        p0: CommandContext,
        p1: Store<EntityStore?>,
        p2: Ref<EntityStore?>,
        p3: PlayerRef,
        p4: World,
    ): CompletableFuture<Void?> {
        val homeName = homeNameArg.get(p0)
        val homeComponent = p1.ensureAndGetComponent(p2, plugin.getPlayerHomeDataComponent())

        if (homeComponent.teleportCooldown > 0) {
            plugin.messenger.sendMessage("You cannot teleport for another ${homeComponent.teleportCooldown.toInt()} seconds.", p0)
            return CompletableFuture.completedFuture(null)
        }

        if (homeComponent.homes.contains(homeName)) {
            val home = homeComponent.homes[homeName]!!
            homeComponent.teleportCooldown = plugin.config.get().timeBetweenTeleport
            val teleport =
                Teleport.createForPlayer(
                    Universe.get().getWorld(UUID.fromString(home.world)),
                    home.position,
                    home.rotation,
                )
            p1.addComponent(p2, Teleport.getComponentType(), teleport)
            plugin.messenger.sendMessage(
                "Teleporting to home $homeName.",
                p0,
            )
        } else {
            plugin.messenger.sendMessage("Home $homeName does not exist. Use /homes to find your home's name.", p0)
        }

        return CompletableFuture.completedFuture(null)
    }
}
