package ru.megaeater42.service;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.megaeater42.telegram.abstraction.TelegramResponse;

import java.util.List;

public interface UpdateProducerService {
    List<TelegramResponse> process(Update update);
}
