package io.github.prostoazya.enigmachat.config

import io.github.prostoazya.enigmachat.ChatHandler
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.minecraft.world.InteractionResult
import org.slf4j.LoggerFactory

object ConfigManager {
    fun init() {
        val holder = AutoConfig.register(ModConfig::class.java) { definition, configClass ->
            Toml4jConfigSerializer(definition, configClass)
        }

        holder.registerSaveListener { _, config ->
            loadConfig(config)
            InteractionResult.SUCCESS
        }

        loadConfig(holder.config)
    }

    private fun loadConfig(config: ModConfig) {
        ChatHandler.enabled = config.enabled
        ChatHandler.key = config.key
    }
}