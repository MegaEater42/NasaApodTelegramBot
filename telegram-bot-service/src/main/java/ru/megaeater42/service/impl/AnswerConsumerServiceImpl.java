package ru.megaeater42.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.*;
import ru.megaeater42.controller.TelegramBot;
import ru.megaeater42.service.AnswerConsumerService;
import ru.megaeater42.telegram.abstraction.TelegramResponse;

import java.util.List;

@Slf4j
@Service
public class AnswerConsumerServiceImpl implements AnswerConsumerService {
    private TelegramBot telegramBot;

    public void registerBot(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void consume(List<TelegramResponse> telegramResponses) {
        for (TelegramResponse telegramResponse : telegramResponses) {
            if (telegramResponse == null) {
                log.warn("Received null answer!");
                return;
            }
            log.info("Received answer: {}", telegramResponse);

            if (telegramResponse.isBotApiMethod()) {
                consume(telegramResponse.getBotApiMethod());
            }
            if (telegramResponse.isSendAnimation()) {
                consume(telegramResponse.getSendAnimation());
            }
            if (telegramResponse.isSendMediaGroup()) {
                consume(telegramResponse.getSendMediaGroup());
            }
            if (telegramResponse.isSendPhoto()) {
                consume(telegramResponse.getSendPhoto());
            }
            if (telegramResponse.isSendVideo()) {
                consume(telegramResponse.getSendVideo());
            }
        }
    }

    @Override
    public void consume(BotApiMethod<?> answer) {
        try {
            telegramBot.execute(answer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void consume(SendAnimation answer) {
        try {
            telegramBot.execute(answer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void consume(SendMediaGroup answer) {
        try {
            telegramBot.execute(answer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void consume(SendPhoto answer) {
        try {
            telegramBot.execute(answer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void consume(SendVideo answer) {
        try {
            telegramBot.execute(answer);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
