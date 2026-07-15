package io.github.prostoazya.enigmachat.mixins;

import io.github.prostoazya.enigmachat.ChatHandler;
import io.github.prostoazya.enigmachat.ChatWriter;
import io.github.prostoazya.enigmachat.Encryption;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {

    @ModifyVariable(
            method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/multiplayer/chat/GuiMessageSource;Lnet/minecraft/client/multiplayer/chat/GuiMessageTag;)V",
            at = @At("HEAD"),
            argsOnly = true,
            name = "contents")
    private Component modifyInboundMessage(Component contents) {
        if (!ChatHandler.INSTANCE.getEnabled()) {
            return contents;
        }

        String fullText = contents.getString();
        if (!fullText.contains("[E]")) {
            return contents;
        }

        try {
            return replaceEncryptedText(contents);
        } catch (Exception e) {
            return contents;
        }
    }

    @Unique
    private Component replaceEncryptedText(Component component) {
        net.minecraft.network.chat.ComponentContents contents = component.getContents();

        if (contents instanceof PlainTextContents.LiteralContents(String text)) {
            if (text.contains("[E]")) {
                try {
                    int index = text.indexOf("[E]");
                    String encryptedPart = text.substring(index + 3).trim();

                    String decryptedText = Encryption.INSTANCE.decrypt(encryptedPart, ChatHandler.INSTANCE.getKey());

                    Component decryptedPart = ChatWriter.INSTANCE.formatMessage(decryptedText, "[E]" + encryptedPart);

                    MutableComponent result;
                    if (index > 0) {
                        String prefix = text.substring(0, index);
                        result = Component.literal(prefix).append(decryptedPart);
                    } else {
                        result = Component.empty().append(decryptedPart);
                    }

                    result.withStyle(component.getStyle());
                    for (Component sibling : component.getSiblings()) {
                        result.append(replaceEncryptedText(sibling));
                    }
                    return result;

                } catch (Exception e) {
                    return component;
                }
            }
        }

        if (contents instanceof TranslatableContents translatable) {
            Object[] args = translatable.getArgs();
            Object[] modifiedArgs = new Object[args.length];

            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof Component argComponent) {
                    modifiedArgs[i] = replaceEncryptedText(argComponent);
                } else {
                    modifiedArgs[i] = arg;
                }
            }

            TranslatableContents newContents = new TranslatableContents(
                    translatable.getKey(),
                    translatable.getFallback(),
                    modifiedArgs
            );

            MutableComponent result = MutableComponent.create(newContents).withStyle(component.getStyle());
            for (Component sibling : component.getSiblings()) {
                result.append(replaceEncryptedText(sibling));
            }
            return result;
        }

        MutableComponent result = MutableComponent.create(contents).withStyle(component.getStyle());
        for (Component sibling : component.getSiblings()) {
            result.append(replaceEncryptedText(sibling));
        }
        return result;
    }
}