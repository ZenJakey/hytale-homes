package com.trinex.homes.commands.home

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.codec.codecs.map.MapCodec
import com.hypixel.hytale.component.Component
import com.hypixel.hytale.math.vector.Vector3d
import com.hypixel.hytale.math.vector.Vector3f
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

class HomePlayerData : Component<EntityStore?> {
    var homes = mutableMapOf<String, Home>()
    var teleportCooldown = 0.0f

    constructor()

    constructor(clone: HomePlayerData) {
        homes = clone.homes
        teleportCooldown = clone.teleportCooldown
    }

    override fun clone(): Component<EntityStore?> = HomePlayerData(this)

    fun decrementCooldown(dt: Float) {
        if (teleportCooldown <= 0) {
            return
        }
        teleportCooldown = (teleportCooldown - dt).coerceAtLeast(0f)
    }

    companion object {
        val CODEC: BuilderCodec<HomePlayerData> =
            BuilderCodec
                .builder<HomePlayerData>(HomePlayerData::class.java, { HomePlayerData() })
                .append(
                    KeyedCodec("TeleportCooldown", Codec.FLOAT),
                    { data, value -> data.teleportCooldown = value },
                    { data -> data.teleportCooldown },
                ).add()
                .append(
                    KeyedCodec("Homes", MapCodec(Home.CODEC, ::HashMap, false)),
                    { data, value -> data.homes = value },
                    { data -> data.homes },
                ).add()
                .build()
    }
}

data class Home(
    var position: Vector3d = Vector3d.ZERO,
    var rotation: Vector3f = Vector3f.ZERO,
    var world: String = "",
) {
    companion object {
        val CODEC: BuilderCodec<Home> =
            BuilderCodec
                .builder<Home>(Home::class.java, { Home() })
                .append(
                    KeyedCodec("Location", Vector3d.CODEC),
                    { data, value -> data.position = value },
                    { data -> data.position },
                ).add()
                .append(
                    KeyedCodec("Rotation", Vector3f.CODEC),
                    { data, value -> data.rotation = value },
                    { data -> data.rotation },
                ).add()
                .append(
                    KeyedCodec("World", Codec.STRING),
                    { data, value -> data.world = value },
                    { data -> data.world },
                ).add()
                .build()
    }
}
