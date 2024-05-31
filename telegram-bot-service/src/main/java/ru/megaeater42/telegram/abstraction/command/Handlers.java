package ru.megaeater42.telegram.abstraction.command;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.megaeater42.entity.User;
import ru.megaeater42.service.UserService;
import ru.megaeater42.telegram.abstraction.TelegramResponse;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.UpdateType;
import ru.megaeater42.telegram.abstraction.utils.Consts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handlers {
    private final HashMap<UpdateType, List<Handler>> commandsRegistry = new HashMap<>();
    private final List<Handler> handlers;

    private final UserService userService;

    @PostConstruct
    private void init() {
        for (Handler handler : handlers) {
            if (!commandsRegistry.containsKey(handler.getHandlingType())) {
                commandsRegistry.put(handler.getHandlingType(), new ArrayList<>());
            }
            commandsRegistry.get(handler.getHandlingType()).add(handler);
            log.warn("{} was added for {} handling", handler.getClass().getSimpleName(), handler.getHandlingType());
        }

        commandsRegistry.values().forEach(h -> h.sort((o1, o2) -> o2.getPriority() - o1.getPriority()));
    }

    public List<TelegramResponse> execute(ClassifiedUpdate classifiedUpdate) {
        User user = userService.findOrCreateUserByClassifiedUpdate(classifiedUpdate);
        if (!commandsRegistry.containsKey(classifiedUpdate.getUpdateType())) {
            return List.of(new TelegramResponse(SendMessage.builder()
                    .chatId(classifiedUpdate.getChatId())
                    .text(Consts.CANT_UNDERSTAND)
                    .build()));
        }

        for (Handler handler : commandsRegistry.get(classifiedUpdate.getUpdateType())) {
            if (handler.getCondition(classifiedUpdate, user)) {
                return handler.apply(classifiedUpdate, user);
            }
        }
        return List.of(new TelegramResponse(SendMessage.builder()
                .chatId(classifiedUpdate.getChatId())
                .text(Consts.UNKNOWN_COMMAND)
                .build()));
    }
}
