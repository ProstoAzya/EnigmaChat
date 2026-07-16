package io.github.prostoazya.enigmachat

import com.mojang.brigadier.arguments.StringArgumentType
import io.github.prostoazya.enigmachat.ChatHandler.enabled
import io.github.prostoazya.enigmachat.ChatHandler.encryptionKey
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.ClientCommands
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.commands.SharedSuggestionProvider
import net.minecraft.network.chat.Component
import kotlin.properties.Delegates

object EWCommand {
    var ewCommandEnabled by Delegates.notNull<Boolean>()
    lateinit var replacementCommand: String

    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                ClientCommands.literal("ew")
                    .then(ClientCommands.argument("target", StringArgumentType.word())
                        .suggests { context, builder ->
                            SharedSuggestionProvider.suggest(context.source.onlinePlayerNames, builder)
                        }
                        .then(ClientCommands.argument("message", StringArgumentType.greedyString())
                            .executes { context ->
                                val player = Minecraft.getInstance().player ?: return@executes 0

                                if (!ewCommandEnabled) return@executes 0
                                if (!enabled) return@executes 0

                                try {
                                    val target = StringArgumentType.getString(context, "target")
                                    val message = StringArgumentType.getString(context, "message")

                                    if (message.isEmpty()) {
                                        val component =
                                            Component.translatable("text.enigmachat.error.empty_message").withStyle(ChatFormatting.RED)
                                        Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(component)
                                        return@executes 0
                                    }
                                    if (encryptionKey.isEmpty()) {
                                        val component =
                                            Component.translatable("text.enigmachat.error.empty_key").withStyle(ChatFormatting.RED)
                                        Minecraft.getInstance().gui.hud.chat.addClientSystemMessage(component)
                                        return@executes 0
                                    }

                                    val encryptedMessage = Encryption.encrypt(message, encryptionKey)
                                    val finalCommand = "${replacementCommand.trim()} $target [E]$encryptedMessage"
                                    player.connection.sendCommand(finalCommand)

                                } catch (e: Exception) {
                                    context.source.sendError(Component.translatable("text.enigmachat.error.default").append(e.localizedMessage))
                                }
                                return@executes 1
                            }
                        )
                    )
            )
        }
    }
}


