package ru.megaeater42.telegram.abstraction.command.impl.simple;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.megaeater42.entity.User;
import ru.megaeater42.entity.enums.State;
import ru.megaeater42.repository.UserRepository;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.TelegramResponse;
import ru.megaeater42.telegram.abstraction.command.AbstractCommandHandler;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RegisterHandler extends AbstractCommandHandler {
    private final UserRepository userRepository;

    @Override
    public Object getRecognizer() {
        return "/register";
    }

    @Override
    public boolean getCondition(ClassifiedUpdate classifiedUpdate, User user) {
        return super.getCondition(classifiedUpdate, user) && !State.isWaitingForText(user.getState().getValue());
    }

    @Override
    public List<TelegramResponse> apply(ClassifiedUpdate classifiedUpdate, User user) {
        user.getState().setValue(State.WAITING_FOR_TEXT);
        userRepository.save(user);
        return List.of(
                new TelegramResponse(
                        SendMessage.builder()
                                .chatId(classifiedUpdate.getChatId())
                                .text("Введите свой NASA API key:")
                                .build()
                )
        );
    }
}