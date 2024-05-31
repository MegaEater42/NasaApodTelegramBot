package ru.megaeater42.telegram.abstraction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;

@NoArgsConstructor
@Getter
public class TelegramResponse {
    private BotApiMethod<?> botApiMethod;
    private SendAnimation sendAnimation;
    private SendMediaGroup sendMediaGroup;
    private SendPhoto sendPhoto;
    private SendVideo sendVideo;

    public TelegramResponse(BotApiMethod<?> botApiMethod) {
        this.botApiMethod = botApiMethod;
    }

    public TelegramResponse(SendAnimation sendAnimation) {
        this.sendAnimation = sendAnimation;
    }

    public TelegramResponse(SendMediaGroup sendMediaGroup) {
        this.sendMediaGroup = sendMediaGroup;
    }

    public TelegramResponse(SendPhoto sendPhoto) {
        this.sendPhoto = sendPhoto;
    }

    public TelegramResponse(SendVideo sendVideo) {
        this.sendVideo = sendVideo;
    }

    public boolean isBotApiMethod() {
        return botApiMethod != null;
    }

    public boolean isSendAnimation() {
        return sendAnimation != null;
    }

    public boolean isSendMediaGroup() {
        return sendMediaGroup != null;
    }

    public boolean isSendPhoto() {
        return sendPhoto != null;
    }

    public boolean isSendVideo() {
        return sendVideo != null;
    }
}