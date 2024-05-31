package ru.megaeater42.telegram.abstraction.command;

import ru.megaeater42.entity.User;
import ru.megaeater42.telegram.abstraction.TelegramResponse;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.UpdateType;

import java.util.List;

public interface Handler {
    UpdateType getHandlingType();

    default int getPriority() {
        return 0;
    }

    default boolean getCondition(ClassifiedUpdate update, User user) {
        return false;
    }

    List<TelegramResponse> apply(ClassifiedUpdate classifiedUpdate, User user);
}