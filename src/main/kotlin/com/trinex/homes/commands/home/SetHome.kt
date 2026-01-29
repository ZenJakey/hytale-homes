package com.trinex.homes.commands.home

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.protocol.GameMode
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncPlayerCommand
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.trinex.homes.Homes
import java.util.concurrent.CompletableFuture

class SetHome(
    val plugin: Homes,
) : AbstractAsyncPlayerCommand("sethome", "Sets a home at your current location. Usage: /sethome [home name]") {
    private val homeNameArg: RequiredArg<String> =
        this.withRequiredArg(
            "name",
            "The name of the home to set.",
            ArgTypes.STRING,
        )

    init {
        setPermissionGroup(GameMode.Adventure) // Allows the command to be used by anyone, not just OP
    }

    override fun executeAsync(
        ctx: CommandContext,
        store: Store<EntityStore?>,
        ref: Ref<EntityStore?>,
        p3: PlayerRef,
        p4: World,
    ): CompletableFuture<Void?> {
        val homeName = homeNameArg.get(ctx)
        val playerTransform =
            store.getComponent(ref, TransformComponent.getComponentType()) ?: return CompletableFuture.completedFuture(null)

        val position = playerTransform.position.clone()
        val rotation = playerTransform.rotation.clone()
        val world = p4.worldConfig.uuid
        if (p4.name != World.DEFAULT) {
            plugin.messenger.sendMessage("You can only set homes in the default world.", ctx)
            return CompletableFuture.completedFuture(null)
        }

        val homeComponent = store.ensureAndGetComponent(ref, plugin.getPlayerHomeDataComponent())

        if (homeComponent.homes.containsKey(homeName)) {
            plugin.messenger.sendMessage("Home $homeName already exists.", ctx)
            return CompletableFuture.completedFuture(null)
        }

        if (homeComponent.homes.size >= plugin.config.get().maxHomes) {
            plugin.messenger.sendMessage("You have reached the maximum number of homes.", ctx)
            return CompletableFuture.completedFuture(null)
        }

        homeComponent.homes[homeName] = Home(position = position, rotation = rotation, world = world.toString())
        plugin.messenger.sendMessage("Home $homeName set.", ctx)

        return CompletableFuture.completedFuture(null)
    }
}
