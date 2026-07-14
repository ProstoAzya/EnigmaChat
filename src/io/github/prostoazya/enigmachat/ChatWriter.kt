package io.github.prostoazya.enigmachat

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent

object ChatWriter {
    fun encryptedMessage(targetName: String, rawMessage: String, encryptedMessage: String): Component {
        val tooltipTitle = Component.translatable("tooltip.enigmachat.encrypted_info")
            .withStyle(ChatFormatting.GRAY)

        val encryptedMessageComponent = Component.literal(encryptedMessage)
            .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)

        val tooltipText = Component.empty()
            .append(tooltipTitle)
            .append(encryptedMessageComponent)

        val enigmaSymbol = Component.translatable("text.enigmachat.enigma_symbol")
            .withStyle { style ->
                style.withColor(ChatFormatting.GOLD)
                    .withBold(true)
                    .withHoverEvent(HoverEvent.ShowText(tooltipText))
            }

        val symbolRT = Component.literal(">").withStyle(ChatFormatting.AQUA)

        val finalMessage = Component.empty()
            .append(enigmaSymbol)
            .append(Component.literal(" [ "))
            .append(symbolRT)
            .append(Component.literal(" $targetName ]"))
            .append(Component.literal(" $rawMessage"))

        return finalMessage
    }

    fun decryptedMessage(senderName: String, rawMessage: String, decryptedMessage: String): Component {
        val tooltipTitle = Component.translatable("tooltip.enigmachat.decrypted_info")
            .withStyle(ChatFormatting.GRAY)

        val rawMessageComponent = Component.literal(rawMessage)
            .withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC)

        val tooltipText = Component.empty()
            .append(tooltipTitle)
            .append(rawMessageComponent)

        val enigmaSymbol = Component.translatable("text.enigmachat.enigma_symbol")
            .withStyle { style ->
                style.withColor(ChatFormatting.GOLD)
                    .withBold(true)
                    .withHoverEvent(HoverEvent.ShowText(tooltipText))
            }

        val symbolRT = Component.literal("<").withStyle(ChatFormatting.GOLD)

        val finalMessage = Component.empty()
            .append(enigmaSymbol)
            .append(Component.literal(" [ "))
            .append(symbolRT)
            .append(Component.literal(" $senderName ]"))
            .append(Component.literal(" $decryptedMessage"))

        return finalMessage
    }
}