package io.github.prostoazya.enigmachat

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent

object ChatWriter {
    fun formatMessage(decryptedMessage: String, rawMessage: String): Component {
        val tooltipTitle = Component.translatable("tooltip.enigmachat.encrypted_info")
            .withStyle(ChatFormatting.GRAY)

        val encryptedMessageComponent = Component.literal(rawMessage)
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

        val finalMessage = Component.empty()
            .append(enigmaSymbol)
            .append(Component.literal(" $decryptedMessage"))

        return finalMessage
    }
}