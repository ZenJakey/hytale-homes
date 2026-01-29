package com.trinex.homes.commands.home

import com.hypixel.hytale.component.ArchetypeChunk
import com.hypixel.hytale.component.CommandBuffer
import com.hypixel.hytale.component.ComponentType
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.component.query.Query
import com.hypixel.hytale.component.system.tick.EntityTickingSystem
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

class HomeTeleportCooldownSystem(
    val homeComponentType: ComponentType<EntityStore?, HomePlayerData>,
) : EntityTickingSystem<EntityStore>() {
    override fun tick(
        dt: Float,
        idx: Int,
        p2: ArchetypeChunk<EntityStore?>,
        store: Store<EntityStore?>,
        p4: CommandBuffer<EntityStore?>,
    ) {
        val homes = p2.getComponent(idx, homeComponentType)
        homes?.decrementCooldown(dt)
    }

    override fun getQuery(): Query<EntityStore?> = Query.and(homeComponentType)
}
