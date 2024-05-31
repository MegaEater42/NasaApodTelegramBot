package ru.megaeater42.telegram.abstraction.command.impl.simple;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.megaeater42.entity.User;
import ru.megaeater42.entity.enums.State;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.TelegramResponse;
import ru.megaeater42.telegram.abstraction.command.AbstractCommandHandler;

import java.util.List;

import static ru.megaeater42.telegram.abstraction.utils.Consts.*;

@Component
public class HelpCommandHandler extends AbstractCommandHandler {
    @Override
    public Object getRecognizer() {
        return "/help";
    }

    @Override
    public boolean getCondition(ClassifiedUpdate classifiedUpdate, User user) {
        return super.getCondition(classifiedUpdate, user) && State.isInState(user.getState().getValue());
    }

    @Override
    public List<TelegramResponse> apply(ClassifiedUpdate classifiedUpdate, User user) {
        return List.of(
                new TelegramResponse(
                        SendMessage.builder()
                                .chatId(classifiedUpdate.getChatId())
                                .text(
                                        switch (user.getState().getValue()) {
                                            case READY_FOR_WORK -> HELP_MESSAGE;
                                            case WAITING_FOR_TEXT -> WAITING_FOR_TEXT_HELP_MESSAGE;
                                            default -> BASIC_HELP_MESSAGE;
                                        }
                                )
                                .build()
                )
        );
    }
}