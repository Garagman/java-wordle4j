package ru.yandex.practicum.exceptions;

public class DictionaryLoadException extends WordleException {
    public DictionaryLoadException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}
