package ru.megaeater42.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.megaeater42.service.UpdateProducerService;
import ru.megaeater42.telegram.abstraction.TelegramResponse;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.command.Handlers;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateProducerServiceImpl implements UpdateProducerService {
    private final Handlers handlers;

    public List<TelegramResponse> process(Update update) {
        if (update == null) {
            log.warn("Received null update!");
            return List.of(new TelegramResponse());
        }
        log.info("Received update: {}", update);

        ClassifiedUpdate classifiedUpdate = new ClassifiedUpdate(update);
        return handlers.execute(classifiedUpdate);
    }
}
