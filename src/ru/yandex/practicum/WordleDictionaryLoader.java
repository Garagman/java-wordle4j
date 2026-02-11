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

        try (BufferedReader dictionaryReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {

            Set<String> fiveLetterWords = dictionaryReader.lines().map(WordleDictionary::normalize).filter(normalizedWord -> normalizedWord.length() == 5).filter(normalizedWord -> normalizedWord.chars().allMatch(Character::isLetter)).collect(Collectors.toSet());

            log.println(String.format("Загружено слов длиной 5 букв: %d", fiveLetterWords.size()));
            return new WordleDictionary(fiveLetterWords);

        } catch (IOException exception) {
            throw new DictionaryLoadException("Ошибка загрузки словаря", exception);
        }

    }
}
