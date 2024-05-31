package ru.megaeater42.telegram.abstraction.command.impl.callback;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.megaeater42.dto.ApodDTO;
import ru.megaeater42.entity.User;
import ru.megaeater42.entity.enums.State;
import ru.megaeater42.proxy.NasaApiServiceProxy;
import ru.megaeater42.telegram.abstraction.TelegramResponse;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.command.AbstractCallbackHandler;
import ru.megaeater42.telegram.abstraction.utils.NasaApiTelegramResponsesUtil;
import ru.megaeater42.telegram.calendar.InlineCalendar;
import ru.megaeater42.telegram.calendar.utils.DateUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.megaeater42.telegram.calendar.utils.InlineCalendarCommandUtil.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class InlineCalendarCallbackHandler extends AbstractCallbackHandler {
    private final NasaApiServiceProxy nasaApiServiceProxy;

    @Override
    public boolean getCondition(ClassifiedUpdate classifiedUpdate, User user) {
        return isClicked(classifiedUpdate.getUpdate());
    }

    @Override
    public List<TelegramResponse> apply(ClassifiedUpdate classifiedUpdate, User user) {
        if (!State.isReadyForWork(user.getState().getValue())) {
            return List.of(
                    new TelegramResponse(
                            DeleteMessage.builder()
                                    .chatId(classifiedUpdate.getChatId())
                                    .messageId(classifiedUpdate.getUpdate().getCallbackQuery().getMessage().getMessageId())
                                    .build()
                    ),
                    new TelegramResponse(
                            SendMessage.builder()
                                    .chatId(classifiedUpdate.getChatId())
                                    .text("Вы удалили свой NASA API! Заполните его, и попробуйте заново.")
                                    .build()
                    )
            );
        }

        if (isInfoButtonClicked(classifiedUpdate.getUpdate())) {
            LocalDate pivotDate = extractInfoPivotDate(classifiedUpdate.getUpdate());
            return List.of(
                    new TelegramResponse(
                            EditMessageText.builder()
                                    .chatId(classifiedUpdate.getChatId())
                                    .messageId(classifiedUpdate.getUpdate().getCallbackQuery().getMessage().getMessageId())
                                    .text((pivotDate != null)? pivotDate + " x ?" : "Выберите нужную дату:")
                                    .replyMarkup(
                                            InlineCalendar.builder()
                                                    .leftBound(LocalDate.of(1995, 6, 15))
                                                    .rightBound(LocalDate.now().plusDays(1))
                                                    .ranged(wasRanged(classifiedUpdate.getUpdate()))
                                                    .pivotDate(pivotDate)
                                                    .build(classifiedUpdate.getUpdate())
                                    )
                                    .build()
                    )
            );
        }

        if (isDateSelected(classifiedUpdate.getUpdate())) {
            return getApodsTelegramResponse(extractDate(classifiedUpdate.getUpdate()), Optional.empty(), classifiedUpdate.getChatId(), classifiedUpdate.getUpdate().getCallbackQuery().getMessage().getMessageId(), user);
        }

        if (isRangeSelected(classifiedUpdate.getUpdate())) {
            String[] range = extractRange(classifiedUpdate.getUpdate()).split(":");
            LocalDate startDate = DateUtil.toDate(range[0]);
            LocalDate endDate = DateUtil.toDate(range[1]);
            if (startDate.isAfter(endDate)) {
                LocalDate tmp = endDate;
                endDate = startDate;
                startDate = tmp;
            }
            return getApodsTelegramResponse(startDate, Optional.of(endDate), classifiedUpdate.getChatId(), classifiedUpdate.getUpdate().getCallbackQuery().getMessage().getMessageId(), user);
        }

        if (isRangeClicked(classifiedUpdate.getUpdate())) {
            LocalDate pivotDate = extractPivotDate(classifiedUpdate.getUpdate());
            return List.of(
                    new TelegramResponse(
                            EditMessageText.builder()
                                    .chatId(classifiedUpdate.getChatId())
                                    .messageId(classifiedUpdate.getUpdate().getCallbackQuery().getMessage().getMessageId())
                                    .text(pivotDate + " x ?")
                                    .replyMarkup(
                                            InlineCalendar.builder()
                                                    .leftBound(LocalDate.of(1995, 6, 15))
                                                    .rightBound(LocalDate.now().plusDays(1))
                                                    .ranged(true)
                                                    .pivotDate(pivotDate)
                                                    .build(classifiedUpdate.getUpdate())
                                    )
                                    .build()
                    )
            );
        }

        return List.of(
                new TelegramResponse(
                        AnswerCallbackQuery.builder()
                                .callbackQueryId(classifiedUpdate.getUpdate().getCallbackQuery().getId())
                                .build()
                )
        );
    }

    private List<TelegramResponse> getApodsTelegramResponse(LocalDate date, Optional<LocalDate> endDate, Long chatId, Integer messageId, User user) {
        try {
            List<ApodDTO.Response.Inner> apods;
            if (endDate.isPresent()) {
                apods = nasaApiServiceProxy.getAPODs(date, endDate.get(), user.getNasaApiKey());
            } else {
                apods = List.of(nasaApiServiceProxy.getAPOD(date, user.getNasaApiKey()));
            }

            List<TelegramResponse> answer = new ArrayList<>();
            answer.add(
                    new TelegramResponse(
                            DeleteMessage.builder()
                                    .chatId(chatId)
                                    .messageId(messageId)
                                    .build()
                    )
            );

            if (apods.isEmpty()) {
                answer.add(
                        new TelegramResponse(
                                SendMessage.builder()
                                        .chatId(user.getState().getChatId())
                                        .text("Для промежутка " + date + " - " + endDate + " нет даных :(\n")
                                        .build()
                        )
                );
                return answer;
            }

            answer.addAll(NasaApiTelegramResponsesUtil.getRandomApodsTelegramResponse(apods, chatId));

            return answer;
        }
        catch (FeignException exception) {
            log.warn(String.valueOf(exception.status()));
            return List.of(
                    new TelegramResponse(
                            DeleteMessage.builder()
                                    .chatId(chatId)
                                    .messageId(messageId)
                                    .build()
                    ),
                    new TelegramResponse(
                            SendMessage.builder()
                                    .chatId(user.getState().getChatId())
                                    .text(
                                            switch (exception.status()) {
                                                case 400, 404 -> "Для " + date + " нет даных :(\n"; // Only for getAPOD
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
}
