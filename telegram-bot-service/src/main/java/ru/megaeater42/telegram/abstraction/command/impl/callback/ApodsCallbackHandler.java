package ru.megaeater42.telegram.abstraction.command.impl.callback;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import ru.megaeater42.dto.ApodDTO;
import ru.megaeater42.entity.User;
import ru.megaeater42.entity.enums.State;
import ru.megaeater42.proxy.NasaApiServiceProxy;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.TelegramResponse;
import ru.megaeater42.telegram.abstraction.command.AbstractCallbackHandler;
import ru.megaeater42.telegram.abstraction.utils.NasaApiTelegramResponsesUtil;
import ru.megaeater42.telegram.calendar.InlineCalendar;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApodsCallbackHandler extends AbstractCallbackHandler {
    private final NasaApiServiceProxy nasaApiServiceProxy;

    @Override
    public boolean getCondition(ClassifiedUpdate update, User user) {
        return update.getUpdate().getCallbackQuery().getData().startsWith("APODS:");
    }

    @Override
    public List<TelegramResponse> apply(ClassifiedUpdate classifiedUpdate, User user) {
        List<TelegramResponse> answer = new ArrayList<>();
        answer.add(
                new TelegramResponse(
                        DeleteMessage.builder()
                                .chatId(classifiedUpdate.getChatId())
                                .messageId(classifiedUpdate.getUpdate().getCallbackQuery().getMessage().getMessageId())
                                .build()
                )
        );
        if (classifiedUpdate.getUpdate().getCallbackQuery().getData().equals("APODS:RANGE")) {
            answer.add(
                    new TelegramResponse(
                            SendMessage.builder()
                                    .chatId(classifiedUpdate.getChatId())
                                    .text("Выберите нужную дату:")
                                    .replyMarkup(
                                            InlineCalendar.builder()
                                                    .leftBound(LocalDate.of(1995, 6, 15))
                                                    .rightBound(LocalDate.now().plusDays(1))
                                                    .ranged(true)
                                                    .build(classifiedUpdate.getUpdate())
                                    )
                                    .build()
                    )
            );
        } else {
            // APODS:RANDOM
            if (!State.isReadyForWork(user.getState().getValue())) {
                return List.of(
                        new TelegramResponse(
                                SendMessage.builder()
                                        .chatId(classifiedUpdate.getChatId())
                                        .text("Вы удалили свой NASA API! Заполните его, и попробуйте заново.")
                                        .build()
                        )
                );
            }
            try {
                Random rand = new Random();

                List<ApodDTO.Response.Inner> apods = nasaApiServiceProxy.getAPODs(rand.nextInt(1, 100), user.getNasaApiKey());

                answer.addAll(NasaApiTelegramResponsesUtil.getRandomApodsTelegramResponse(apods, classifiedUpdate.getChatId()));

                return answer;
            }
            catch (FeignException exception) {
                log.warn(String.valueOf(exception.status()));
                return List.of(
                        new TelegramResponse(
                                SendMessage.builder()
                                        .chatId(user.getState().getChatId())
                                        .text(
                                                switch (exception.status()) {
                                                    case 429 -> "Вы превысили лимит запросов NASA API. Попробуйте через 1 час!";
                                                    case 505 -> "NASA API перестал работать... :(\nПопробуйте чуть позже!";
                                                    default -> "Что-то случилось с нашим ботом... :(\nПопробуйте чуть позже!";
                                                }
                                        )
                                        .build()
                        )
                );
            }
        }
        return answer;
    }
}
