package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.DictionaryLoadException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

public class WordleDictionaryLoader {

    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary load(String fileName) throws DictionaryLoadException {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {

            Set<String> words = reader.lines().map(WordleDictionary::normalize).filter(w -> w.length() == 5).filter(w -> w.chars().allMatch(Character::isLetter)).collect(Collectors.toSet());

            log.println("Загружено слов: " + words.size());
            return new WordleDictionary(words);

        } catch (IOException e) {
            throw new DictionaryLoadException("Ошибка загрузки словаря", e);
        }

    }
}
