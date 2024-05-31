package ru.megaeater42.telegram.abstraction.utils;

public class Consts {
    public static final String START_MESSAGE =
            """
            Добро пожаловать, %s!
            Здесь Вы сможете получить завораживающие астрономические картины с помощью NASA API.
            
            Познакомьтесь с доступными командами: /help
            """;
    public static final String BASIC_HELP_MESSAGE =
            """
            Доступные вам команды бота:
            /register - Зарегистрировать новый NASA API key
            /cancel - Отменить текущую операцию
            
            /start - Приветствие
            /help - Список команд
            """;
    public static final String WAITING_FOR_TEXT_HELP_MESSAGE =
            """
            Доступные вам команды бота:
            /cancel - Отменить текущую операцию
            /help - Список команд
            """;

    public static final String HELP_MESSAGE =
            """
            Доступные вам команды бота:
            /register - Зарегистрировать новый NASA API key
            /apod - Получить астрономическую карту дня
            /apods - Получить несколько астрономических карт дня
            /logout - Удалить свой NASA API key
            /cancel - Отменить текущую операцию
            
            /start - Приветствие
            /help - Список команд
            """;

    public static final String UNKNOWN_COMMAND = "Неподдерживаемая команда!";
    public static final String CANT_UNDERSTAND = "Неподдерживаемый тип сообщения!";
}
