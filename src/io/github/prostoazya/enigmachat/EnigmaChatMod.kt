package io.github.prostoazya.enigmachat

import io.github.prostoazya.enigmachat.config.ModConfig
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.fabricmc.api.ClientModInitializer
import net.minecraft.world.InteractionResult

class EnigmaChatMod: ClientModInitializer {
    override fun onInitializeClient() {
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