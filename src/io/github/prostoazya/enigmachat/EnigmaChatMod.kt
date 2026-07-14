package io.github.prostoazya.enigmachat

import io.github.prostoazya.enigmachat.config.ConfigManager
import net.fabricmc.api.ClientModInitializer

class EnigmaChatMod: ClientModInitializer {
    override fun onInitializeClient() {
        ConfigManager.init()

        ChatHandler.register()
    }
}