package ru.megaeater42.telegram.abstraction.command.impl.text;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import ru.megaeater42.entity.User;
import ru.megaeater42.entity.enums.State;
import ru.megaeater42.proxy.NasaApiServiceProxy;
import ru.megaeater42.repository.UserRepository;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.TelegramResponse;
import ru.megaeater42.telegram.abstraction.command.AbstractTextHandler;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NasaApiKeyInputTextHandler extends AbstractTextHandler {
    private final UserRepository userRepository;
    private final NasaApiServiceProxy nasaApiServiceProxy;

    @Override
    public List<TelegramResponse> apply(ClassifiedUpdate classifiedUpdate, User user) {
        try {
            nasaApiServiceProxy.getAPOD(LocalDate.now().minusDays(1), classifiedUpdate.getUpdate().getMessage().getText());
            user.setNasaApiKey(classifiedUpdate.getUpdate().getMessage().getText());
            user.getState().setValue(State.READY_FOR_WORK);
            userRepository.save(user);

            return List.of(
                    new TelegramResponse(
                            DeleteMessage.builder()
                                    .chatId(user.getState().getChatId())
                                    .messageId(classifiedUpdate.getUpdate().getMessage().getMessageId())
                                    .build()
                    ),
                    new TelegramResponse(
                            SendMessage.builder()
                                    .chatId(user.getState().getChatId())
                                    .text("Ваш NASA API key был успешно зарегистрирован!")
                                    .build()
                    )
            );
        }
        catch (FeignException exception) {
            log.error(exception.getMessage());
            return List.of(
                    new TelegramResponse(
                            SendMessage.builder()
                                    .chatId(user.getState().getChatId())
                                    .text(
                                            switch (exception.status()) {
                                                case 403 -> "Вы ввели не подходящий NASA API key. Попробуйте взять другой на https://api.nasa.gov/";
                                                case 404, 505 -> "NASA API перестал работать... :(\nПопробуйте чуть позже!";
                                                case 429 -> "Вы превысили лимит запросов NASA API. Попробуйте через 1 час!";
                                                default -> "Что-то случилось с нашим ботом... :(\nПопробуйте чуть позже!";
                                            }
                                    )
                                    .build()
                    )
            );
        }
    }
}
