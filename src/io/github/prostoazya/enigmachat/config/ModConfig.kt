package io.github.prostoazya.enigmachat.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config

@Config(name = "enigmachat")
class ModConfig: ConfigData {
    val enabled: Boolean = true

    val key: String = "lemon"
}