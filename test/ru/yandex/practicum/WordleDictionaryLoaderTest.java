package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.DictionaryLoadException;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {

    @Test
    void load_shouldLoadOnlyFiveLetterWords() throws Exception {

        Path tempFile = Files.createTempFile("words", ".txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(tempFile.toFile()))) {
            writer.println("ведро");
            writer.println("дом");
            writer.println("окно1");
            writer.println("машина");
        }

        WordleDictionaryLoader loader = new WordleDictionaryLoader(new PrintWriter(System.out));

        WordleDictionary dict = loader.load(tempFile.toString());

        assertTrue(dict.contains("ведро"));
        assertFalse(dict.contains("дом"));
        assertFalse(dict.contains("окно1"));
        assertFalse(dict.contains("машина"));
    }

    @Test
    void load_shouldThrowExceptionOnMissingFile() {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(new PrintWriter(System.out));

        assertThrows(DictionaryLoadException.class, () -> loader.load("no_such_file.txt"));
    }

}
