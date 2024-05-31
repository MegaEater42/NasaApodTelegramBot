package ru.megaeater42.telegram.abstraction.command;

import ru.megaeater42.telegram.abstraction.UpdateType;

public abstract class AbstractCallbackHandler implements Handler {
    @Override
    public UpdateType getHandlingType() {
        return UpdateType.CALLBACK;
    }
}
