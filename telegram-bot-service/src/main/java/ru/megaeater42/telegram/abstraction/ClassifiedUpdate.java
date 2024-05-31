package ru.megaeater42.telegram.abstraction;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Slf4j
public class ClassifiedUpdate {
    private Long chatId;
    private String userName;
    private String firstName;
    private String lastName;
    private final Update update;

    private final UpdateType updateType;

    public ClassifiedUpdate(Update update) {
        this.update = update;
        this.updateType = handleUpdateType();
        handleUserInfo();
    }

    private UpdateType handleUpdateType() {
        if(update.hasCallbackQuery()) {
            return UpdateType.CALLBACK;
        }

        if(update.hasMessage()) {
            if(update.getMessage().hasText()) {
                if(update.getMessage().getText().startsWith("/")) {
                    return UpdateType.COMMAND;
                } else {
                    return UpdateType.TEXT;
                }
            }
        }

        return UpdateType.UNKNOWN;
    }

    private void handleUserInfo() {
        if (updateType.equals(UpdateType.CALLBACK)) {
            firstName = update.getCallbackQuery().getFrom().getFirstName();
            lastName = update.getCallbackQuery().getFrom().getLastName();
            userName = update.getCallbackQuery().getFrom().getUserName();
            chatId = update.getCallbackQuery().getFrom().getId();
        } else {
            if (updateType.equals(UpdateType.COMMAND) || updateType.equals(UpdateType.TEXT)) {
                firstName = update.getMessage().getFrom().getFirstName();
                lastName = update.getMessage().getFrom().getLastName();
                userName = update.getMessage().getFrom().getUserName();
                chatId = update.getMessage().getFrom().getId();
            }
        }
    }

    public String getUserAppeal() {
        return ((lastName != null)? lastName + " " : "") + ((userName != null)? "@" + userName + " " : "") + ((firstName != null)? firstName : "");
    }
}
