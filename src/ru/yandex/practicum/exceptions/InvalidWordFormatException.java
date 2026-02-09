package ru.yandex.practicum.exceptions;

public class InvalidWordFormatException extends GameException {
    public InvalidWordFormatException(String word) {
        super("Некорректный ввод: " + word + ". Слово должно состоять из 5 букв и написано на Русском языке!");
    }
}
