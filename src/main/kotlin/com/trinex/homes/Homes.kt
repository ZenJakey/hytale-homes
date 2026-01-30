package com.trinex.homes

import com.hypixel.hytale.component.ComponentType
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.JavaPluginInit
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore
import com.trinex.homes.commands.home.DelHome
import com.trinex.homes.commands.home.HomePlayerData
import com.trinex.homes.commands.home.HomeTeleportCooldownSystem
import com.trinex.homes.commands.home.ListHomes
import com.trinex.homes.commands.home.SetHome
import com.trinex.homes.commands.home.TeleHome
import com.trinex.lib.messenger.Messenger

class Homes(
    init: JavaPluginInit,
) : JavaPlugin(init) {
    val config = this.withConfig("Homes", HomesConfig.CODEC)
    private val LOGGER = HytaleLogger.forEnclosingClass()
    private lateinit var homePlayerDataComponent: ComponentType<EntityStore?, HomePlayerData>
    val messenger = Messenger("Homes")

    override fun setup() {
        LOGGER.atInfo().log("Setting up plugin " + this.name)

        this.commandRegistry.registerCommand(SetHome(this))
        this.commandRegistry.registerCommand(ListHomes(this))
        this.commandRegistry.registerCommand(DelHome(this))
        this.commandRegistry.registerCommand(TeleHome(this))

        this.homePlayerDataComponent =
            this.entityStoreRegistry.registerComponent(
                HomePlayerData::class.java,
                "HomePlayerDataComponent",
                HomePlayerData.CODEC,
            )

        this.entityStoreRegistry.registerSystem(HomeTeleportCooldownSystem(homePlayerDataComponent))
        config.save()
    }

    fun getPlayerHomeDataComponent(): ComponentType<EntityStore?, HomePlayerData> = homePlayerDataComponent
}
