package ru.megaeater42.service;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import ru.megaeater42.controller.TelegramBot;
import ru.megaeater42.telegram.abstraction.TelegramResponse;

import java.util.List;

public interface AnswerConsumerService {
    void registerBot(TelegramBot telegramBot);

    void consume(List<TelegramResponse> telegramResponse);
    void consume(BotApiMethod<?> answer);
    void consume(SendAnimation answer);
    void consume(SendMediaGroup answer);
    void consume(SendPhoto answer);
    void consume(SendVideo answer);
}
