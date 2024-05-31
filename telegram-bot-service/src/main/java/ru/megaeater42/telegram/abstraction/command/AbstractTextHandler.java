package ru.megaeater42.telegram.abstraction.command;

import ru.megaeater42.entity.User;
import ru.megaeater42.entity.enums.State;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.UpdateType;

public abstract class AbstractTextHandler implements Handler {
    @Override
    public UpdateType getHandlingType() {
        return UpdateType.TEXT;
    }

    @Override
    public boolean getCondition(ClassifiedUpdate classifiedUpdate, User user) {
        return State.isWaitingForText(user.getState().getValue());
    }
}
