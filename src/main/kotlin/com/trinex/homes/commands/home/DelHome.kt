package com.trinex.homes.commands.home

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.protocol.GameMode
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.trinex.homes.Homes
import java.util.concurrent.CompletableFuture

class DelHome(
    val plugin: Homes,
) : AbstractAsyncPlayerCommand("delhome", "Deletes the named home. Usage: /delhome [home name]") {
    private val homeNameArg: RequiredArg<String> = this.withRequiredArg("name", "The name of the home to delete.", ArgTypes.STRING)

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
        if (homeComponent.homes.containsKey(homeName)) {
            homeComponent.homes.remove(homeName)
            plugin.messenger.sendMessage("Home $homeName deleted.", p0)
        } else {
            plugin.messenger.sendMessage("Home $homeName does not exist.", p0)
        }

        return CompletableFuture.completedFuture(null)
    }
}
