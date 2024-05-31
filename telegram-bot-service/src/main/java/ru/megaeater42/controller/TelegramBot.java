package ru.megaeater42.controller;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.megaeater42.service.AnswerConsumerService;
import ru.megaeater42.service.UpdateProducerService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final UpdateProducerService updateProducerService;
    private final AnswerConsumerService answerConsumerService;

    @Getter
    @Value("${bot.username}")
    private String botUsername;
    @Getter
    @Value("${bot.token}")
    private String botToken;

    @PostConstruct
    public void init() {
        answerConsumerService.registerBot(this);
        try {
            List<BotCommand> myCommands = new ArrayList<>();
            myCommands.add(new BotCommand("/register", "Зарегистрировать новый NASA API key"));
            myCommands.add(new BotCommand("/apod", "Получить астрономическую карту дня"));
            myCommands.add(new BotCommand("/apods", "Получить несколько астрономических карт дня"));
            myCommands.add(new BotCommand("/logout", "Удалить свой NASA API key"));
            myCommands.add(new BotCommand("/cancel", "Отменить текущую операцию"));

            myCommands.add(new BotCommand("/start", "Приветствие"));
            myCommands.add(new BotCommand("/help", "Список команд"));

            execute(new SetMyCommands(myCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        new Thread(() -> CompletableFuture.supplyAsync(() -> updateProducerService.process(update)).thenAccept(answerConsumerService::consume)).start();
    }
}