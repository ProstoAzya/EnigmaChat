package io.github.prostoazya.enigmachat.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry

@Config(name = "enigmachat")
class ModConfig: ConfigData {
    val enabled: Boolean = true

    val encryptionKey: String = "lemon"
    val shuffleKey: String = "watermelon"

    @ConfigEntry.Gui.CollapsibleObject
    val ewCommandSettings: EWCommandSettings = EWCommandSettings()

    @ConfigEntry.Gui.CollapsibleObject
    val builtInCommandsSettings: BuiltInCommandsSettings = BuiltInCommandsSettings()

    class EWCommandSettings {
        val useEWCommand: Boolean = true
        @ConfigEntry.Gui.Tooltip(count = 3)
        var replacementCommand: String = "msg"
    }

    class BuiltInCommandsSettings {
        @ConfigEntry.Gui.Tooltip(count = 1)
        val useBuiltInCommands: Boolean = false
        @ConfigEntry.Gui.Tooltip(count = 4)
        var replaceableCommands: String = "w"
    }
}