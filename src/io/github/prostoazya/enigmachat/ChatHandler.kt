package io.github.prostoazya.enigmachat

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import kotlin.properties.Delegates

object ChatHandler {
    var enabled by Delegates.notNull<Boolean>()
    lateinit var key: String
    var lastTargetName: String = "?"

    fun register() {
        registerOutgoingListener()
        registerIncomingListener()
    }

    private fun registerOutgoingListener() {
        ClientSendMessageEvents.ALLOW_COMMAND.register { command ->
            if (!enabled) return@register true

            val regex = """^(msg|tell|w|reply|r)\s+([^\s]+)\s+(.+)$""".toRegex()
            val matchResult = regex.find(command)

            if (matchResult?.groupValues?.size == 4) {
                val command = matchResult.groupValues[1]
                val target = matchResult.groupValues[2]
                val message = matchResult.groupValues[3]

                if (message.isEmpty()) {
                    val component = Component.translatable("text.enigmachat.error.empty_message").withStyle(ChatFormatting.RED)
                    Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(component)
                    return@register true
                }
                if (key.isEmpty()) {
                    val component = Component.translatable("text.enigmachat.error.empty_key").withStyle(ChatFormatting.RED)
                    Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(component)
                    return@register true
                }

                if (message.startsWith("[E]")) return@register true

                val encryptedMessage = Encryption.encrypt(message, key)
                val newCommand = "$command $target [E]$encryptedMessage"
                lastTargetName = target
                Minecraft.getInstance().player?.connection?.sendCommand(newCommand)
                return@register false
            }
            return@register true
        }
    }

    private fun registerIncomingListener() {
        ClientReceiveMessageEvents.ALLOW_CHAT.register { _, msg, sender, _, _ ->
            if (!enabled) return@register true

            val rawMessage = msg?.signedBody()?.content

            lateinit var senderName: String
            if (sender?.name == null) {
                senderName = "?"
            } else {
                senderName = sender.name
            }

            if (rawMessage?.startsWith("[E]") == true) {
                val message = rawMessage.substring(3)

                if (key.isEmpty()) {
                    val component = Component.translatable("text.enigmachat.error.empty_key").withStyle(ChatFormatting.RED)
                    Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(component)
                    return@register true
                }

                try {
                    val decryptedMessage = Encryption.decrypt(message, key)
                    val component: Component = if ( sender?.id == Minecraft.getInstance().player?.uuid) {
                        ChatWriter.encryptedMessage(lastTargetName, decryptedMessage, rawMessage)
                    } else {
                        ChatWriter.decryptedMessage(senderName, rawMessage, decryptedMessage)
                    }

                    Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(component)
                    return@register false
                }
                catch (e: Exception) {
                    val component = Component.literal(e.toString()).withStyle(ChatFormatting.RED)
                    Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(component)
                    return@register true
                }
            }
            return@register true
        }
    }
}