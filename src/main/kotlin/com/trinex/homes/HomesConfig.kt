package com.trinex.homes

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec

object HomesConfig {
    val CODEC: BuilderCodec<HomesConfig> =
        BuilderCodec
            .builder<HomesConfig>(HomesConfig::class.java, { HomesConfig })
            .append(
                KeyedCodec("MaxHomes", Codec.INTEGER),
                { config, value: Int -> config.maxHomes = value }, // Setter
                { config -> config.maxHomes },
            ).add() // Getter
            .append(
                KeyedCodec("TimeBetweenTeleport", Codec.FLOAT),
                { config, value -> config.timeBetweenTeleport = value }, // Setter
                { config -> config.timeBetweenTeleport },
            ).add() // Getter
            .build()

    var maxHomes: Int = 1
    var timeBetweenTeleport = 30f
}
