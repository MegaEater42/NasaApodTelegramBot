package ru.megaeater42.entity.enums;

public enum State {
    BASIC_ACCESS,
    READY_FOR_WORK,

    WAITING_FOR_TEXT;

    public static boolean isInState(State state) {
        return state != null;
    }

    public static boolean isBasicAccess(State state) {
        return isInState(state) && state.equals(BASIC_ACCESS);
    }

    public static boolean isReadyForWork(State state) {
        return isInState(state) && state.equals(READY_FOR_WORK);
    }

    public static boolean isWaitingForText(State state) {
        return isInState(state) && state.equals(WAITING_FOR_TEXT);
    }

    public static boolean canLogout(State state) {
        return !State.isBasicAccess(state) && !State.isWaitingForText(state);
    }
}