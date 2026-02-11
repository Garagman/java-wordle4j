package ru.yandex.practicum.exceptions;

public class InvalidWordFormatException extends GameException {

    private static final String MESSAGE_TEMPLATE = "Некорректный ввод: %s. Слово должно состоять из 5 букв и быть написано на Русском языке!";

    public InvalidWordFormatException(String word) {
        super(String.format(MESSAGE_TEMPLATE, word));
    }
}
