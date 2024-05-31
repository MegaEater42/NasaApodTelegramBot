package ru.megaeater42.telegram.abstraction.command;

import ru.megaeater42.entity.User;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;
import ru.megaeater42.telegram.abstraction.UpdateType;

public abstract class AbstractCommandHandler implements Handler {
    @Override
    public UpdateType getHandlingType() {
        return UpdateType.COMMAND;
    }

    public abstract Object getRecognizer();

    @Override
    public boolean getCondition(ClassifiedUpdate classifiedUpdate, User user) {
        return classifiedUpdate.getUpdate().getMessage().getText().split(" ")[0].equals(getRecognizer());
    }
}
