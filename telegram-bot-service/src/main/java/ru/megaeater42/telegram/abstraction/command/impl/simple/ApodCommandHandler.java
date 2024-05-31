package ru.megaeater42.telegram.abstraction.command.impl.simple;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.megaeater42.entity.User;
import ru.megaeater42.entity.enums.State;
import ru.megaeater42.telegram.abstraction.TelegramResponse;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.command.AbstractCommandHandler;
import ru.megaeater42.telegram.calendar.InlineCalendar;

import java.time.LocalDate;
import java.util.List;

@Component
public class ApodCommandHandler extends AbstractCommandHandler {
    @Override
    public Object getRecognizer() {
        return "/apod";
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
                                .text("Выберите нужную дату:")
                                .replyMarkup(
                                        InlineCalendar.builder()
                                                .leftBound(LocalDate.of(1995, 6, 15))
                                                .rightBound(LocalDate.now().plusDays(1))
                                                .build(classifiedUpdate.getUpdate())
                                )
                                .build()
                )
        );
    }
}
