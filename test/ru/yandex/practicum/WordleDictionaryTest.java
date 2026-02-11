package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    @Test
    void normalize_shouldTrimAndLowercase() {
        String result = WordleDictionary.normalize("  ВеДрО ");
        assertEquals("ведро", result);
    }

    @Test
    void contains_shouldFindWord() {
        WordleDictionary dict = new WordleDictionary(Set.of("ведро", "домик"));
        assertTrue(dict.contains("ведро"));
        assertFalse(dict.contains("окно"));
    }

    @Test
    void analyze_allLettersCorrect() {
        String hint = WordleDictionary.analyze("ведро", "ведро");
        assertEquals("+++++", hint);
    }

    @Test
    void analyze_noLettersMatch() {
        String hint = WordleDictionary.analyze("aaaaa", "ведро");
        assertEquals("-----", hint);
    }

    @Test
    void analyze_withDuplicates() {
        String hint = WordleDictionary.analyze("лалал", "аллая");
        assertEquals("^^++-", hint);
    }
}
