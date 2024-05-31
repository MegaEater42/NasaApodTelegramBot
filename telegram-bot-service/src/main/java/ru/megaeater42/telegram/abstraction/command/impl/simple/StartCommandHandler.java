package ru.megaeater42.telegram.abstraction.command.impl.simple;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.megaeater42.entity.User;
import ru.megaeater42.telegram.abstraction.TelegramResponse;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.command.AbstractCommandHandler;
import ru.megaeater42.telegram.abstraction.utils.Consts;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommandHandler extends AbstractCommandHandler {
    @Override
    public Object getRecognizer() {
        return "/start";
    }

    @Override
    public List<TelegramResponse> apply(ClassifiedUpdate classifiedUpdate, User user) {
        return List.of(
                new TelegramResponse(
                        SendMessage.builder()
                                .chatId(classifiedUpdate.getChatId())
                                .text(String.format(Consts.START_MESSAGE, classifiedUpdate.getUserAppeal()))
                                .build()
                )
        );
    }
}
