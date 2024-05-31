package ru.megaeater42.telegram.abstraction.command.impl.simple;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.megaeater42.entity.User;
import ru.megaeater42.entity.enums.State;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.TelegramResponse;
import ru.megaeater42.telegram.abstraction.command.AbstractCommandHandler;

import java.util.List;

@Component
public class ApodsCommandHandler extends AbstractCommandHandler {
    @Override
    public Object getRecognizer() {
        return "/apods";
    }

    @Override
    public boolean getCondition(ClassifiedUpdate classifiedUpdate, User user) {
        return super.getCondition(classifiedUpdate, user) && State.isReadyForWork(user.getState().getValue());
    }

    @Override
    public List<TelegramResponse> apply(ClassifiedUpdate classifiedUpdate, User user) {
        return List.of(
                new TelegramResponse(
                        SendMessage.builder()
                                .chatId(classifiedUpdate.getChatId())
                                .text("Выберите режим:")
                                .replyMarkup(
                                        InlineKeyboardMarkup.builder()
                                                .keyboardRow(List.of(
                                                        InlineKeyboardButton.builder()
                                                                .text("За промежуток")
                                                                .callbackData("APODS:RANGE")
                                                                .build(),
                                                        InlineKeyboardButton.builder()
                                                                .text("Случайно")
                                                                .callbackData("APODS:RANDOM")
                                                                .build()
                                                ))
                                                .build()
                                )
                                .build()
                )
        );
    }
}
