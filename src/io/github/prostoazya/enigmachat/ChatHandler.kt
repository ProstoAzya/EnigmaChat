package io.github.prostoazya.enigmachat

import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import kotlin.properties.Delegates

object ChatHandler {
    var enabled by Delegates.notNull<Boolean>()
    lateinit var encryptionKey: String

    var builtInCommandsEnabled by Delegates.notNull<Boolean>()
    lateinit var replaceableCommands: String

    fun register() {
        registerOutgoingListener()
    }

    private fun registerOutgoingListener() {
        ClientSendMessageEvents.ALLOW_COMMAND.register { command ->
            if (!enabled) return@register true
            if (!builtInCommandsEnabled) return@register true

            val formattedCommands = replaceableCommands.trim().replace(Regex("\\s+"), "|")

            val regex = """^($formattedCommands)\s+([^\s]+)\s+(.+)$""".toRegex()
            val matchResult = regex.find(command)

            if (matchResult?.groupValues?.size == 4) {
                val command = matchResult.groupValues[1]
                val target = matchResult.groupValues[2]
                val message = matchResult.groupValues[3]

                if (message.isEmpty()) {
                    val component =
                        Component.translatable("text.enigmachat.error.empty_message").withStyle(ChatFormatting.RED)
                    Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(component)
                    return@register true
                }
                if (encryptionKey.isEmpty()) {
                    val component =
                        Component.translatable("text.enigmachat.error.empty_key").withStyle(ChatFormatting.RED)
                    Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(component)
                    return@register true
                }

                if (message.startsWith("[E]")) return@register true

                val encryptedMessage = Encryption.encrypt(message, encryptionKey)
                val newCommand = "$command $target [E]$encryptedMessage"
                Minecraft.getInstance().player?.connection?.sendCommand(newCommand)
                return@register false
            }
            return@register true
        }
    }
}