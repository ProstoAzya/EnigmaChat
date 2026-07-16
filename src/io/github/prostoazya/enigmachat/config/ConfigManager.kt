package io.github.prostoazya.enigmachat.config

import io.github.prostoazya.enigmachat.ChatHandler
import io.github.prostoazya.enigmachat.EWCommand
import io.github.prostoazya.enigmachat.Encryption
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.minecraft.world.InteractionResult

object ConfigManager {
    fun init() {
        val holder = AutoConfig.register(ModConfig::class.java) { definition, configClass ->
            Toml4jConfigSerializer(definition, configClass)
        }

        holder.registerSaveListener { _, config ->
            val allowedPattern1 = """^[a-zA-Zа-яА-ЯёЁ]*$""".toRegex()
            val allowedPattern2 = """^[a-zA-Zа-яА-ЯёЁ\s]*$""".toRegex()

            if (config.ewCommandSettings.replacementCommand.isEmpty() || !allowedPattern1.matches(config.ewCommandSettings.replacementCommand)) {
                config.ewCommandSettings.replacementCommand = "msg"
            }
            if (config.builtInCommandsSettings.replaceableCommands.isEmpty() || !allowedPattern2.matches(config.builtInCommandsSettings.replaceableCommands)) {
                config.builtInCommandsSettings.replaceableCommands = "w"
            }

            loadConfig(config)
            InteractionResult.SUCCESS
        }

        loadConfig(holder.config)
    }

    private fun loadConfig(config: ModConfig) {
        ChatHandler.enabled = config.enabled
        ChatHandler.encryptionKey = config.encryptionKey
        Encryption.updateAlphabet(config.shuffleKey)

        EWCommand.ewCommandEnabled = config.ewCommandSettings.useEWCommand
        EWCommand.replacementCommand = config.ewCommandSettings.replacementCommand

        ChatHandler.builtInCommandsEnabled = config.builtInCommandsSettings.useBuiltInCommands
        ChatHandler.replaceableCommands = config.builtInCommandsSettings.replaceableCommands
    }
}