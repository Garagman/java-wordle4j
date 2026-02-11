package ru.yandex.practicum.exceptions;

public class WordNotFoundInDictionary extends GameException {

    private static final String MESSAGE_TEMPLATE = "Слово отсутствует в словаре %s";

    public WordNotFoundInDictionary(String word) {
        super(String.format(MESSAGE_TEMPLATE, word));
    }
}
