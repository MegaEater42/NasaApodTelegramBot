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
public class CancelCommandHandler  extends AbstractCommandHandler {
    private final UserRepository userRepository;

    @Override
    public Object getRecognizer() {
        return "/cancel";
    }

    @Override
    public List<TelegramResponse> apply(ClassifiedUpdate classifiedUpdate, User user) {
        if (user.getNasaApiKey() != null) {
            user.getState().setValue(State.READY_FOR_WORK);
        } else {
            user.getState().setValue(State.BASIC_ACCESS);
        }
        userRepository.save(user);
        return List.of(
                new TelegramResponse(
                        SendMessage.builder()
                                .chatId(classifiedUpdate.getChatId())
                                .text("Что бы мы там не делали, я обо всём забыл! O_o")
                                .build()
                )
        );
    }
}
