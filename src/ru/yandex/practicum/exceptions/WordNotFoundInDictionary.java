package ru.yandex.practicum.exceptions;

public class WordNotFoundInDictionary extends GameException {
    public WordNotFoundInDictionary(String word) {
        super("Слово отсутствует в словаре: " + word);
    }
}
