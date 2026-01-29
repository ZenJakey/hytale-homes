package com.trinex.homes.commands.home

import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.protocol.GameMode
import com.hypixel.hytale.server.core.command.system.CommandContext
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractAsyncPlayerCommand
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.Universe
import com.hypixel.hytale.server.core.universe.world.World
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.trinex.homes.Homes
import java.util.UUID
import java.util.concurrent.CompletableFuture

class ListHomes(
    val plugin: Homes,
) : AbstractAsyncPlayerCommand("homes", "Lists all of your homes.") {
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
        val homeComponent = p1.ensureAndGetComponent(p2, plugin.getPlayerHomeDataComponent())

        if (homeComponent.homes.isEmpty()) {
            plugin.messenger.sendMessage("You have no homes.", p0)
        } else {
            val homesString =
                buildList {
                    homeComponent.homes.forEach { (name, home) ->
                        add(
                            "$name: ${Universe.get().getWorld(
                                UUID.fromString(home.world),
                            )?.name} - X=${home.position.x.toInt()}, Y=${home.position.y.toInt()}, Z=${home.position.z.toInt()}",
                        )
                    }
                }
            plugin.messenger.sendMessage("Your homes:\n${homesString.joinToString(separator = "\n")}", p0)
        }

        return CompletableFuture.completedFuture(null)
    }
}
