package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.*;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {

    private static final int WORD_LENGTH = 5;
    private static final int MAX_STEPS = 6;

    private final String answer;
    private final PrintWriter log;
    private final WordleDictionary dictionary;

    private int stepsLeft = MAX_STEPS;

    private final Set<String> usedWords = new HashSet<>();
    private final Map<Integer, Character> knownPosition = new HashMap<>();

    private boolean winByHint = false;
    private boolean awaitingFinalHint = false;

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.dictionary = dictionary;
        this.log = log;

        this.answer = dictionary.getRandomWord(new Random());

        log.println("Ответ (для отладки): " + answer);
    }

    //Конструктор для выполнения Юнит теста
    public WordleGame(WordleDictionary dictionary, PrintWriter log, String fixedAnswer) {
        this.dictionary = dictionary;
        this.log = log;
        this.answer = fixedAnswer;
        this.stepsLeft = MAX_STEPS;

        log.println("Ответ (тестовый): " + answer);
    }

    public int getStepsLeft() {
        return stepsLeft;
    }

    public boolean isFinished() {
        return isWin() || stepsLeft <= 0;
    }

    public boolean isWin() {
        return usedWords.contains(answer);
    }

    public boolean isWinByHint() {
        return winByHint;
    }

    public String getAnswer() {
        return answer;
    }

    public String makeMove(String input) throws GameException {

        String word = WordleDictionary.normalize(input);
        validateWord(word);

        awaitingFinalHint = false;

        stepsLeft--;
        usedWords.add(word);

        String hint = WordleDictionary.analyze(word, answer);
        updateKnownPositions(word, hint);

        log.println("Ход: " + word + " -> " + hint);
        return hint;

    }

    public String suggestWord() throws GameException {

        if (awaitingFinalHint) {

            stepsLeft--;
            awaitingFinalHint = false;
            winByHint = true;

            usedWords.add(answer);

            String hint = WordleDictionary.analyze(answer, answer);
            log.println("Подсказка – принудительная победа: " + answer);

            return answer + "\n" + hint;
        }

        if (stepsLeft == 1) {
            awaitingFinalHint = true;
            return "Внимание: следующая подсказка будет правильным ответом. Постарайтесь ответить самостоятельно!!!";
        }

        String hintWord;

        try {
            hintWord = findOnePlusHintWord();
        } catch (GameException e) {
            awaitingFinalHint = true;
            return "Внимание: следующая подсказка будет правильным ответом. Постарайтесь ответить самостоятельно!!!";
        }

        stepsLeft--;
        usedWords.add(hintWord);

        String hint = WordleDictionary.analyze(hintWord, answer);
        updateKnownPositions(hintWord, hint);

        log.println("Подсказка: " + hintWord + " -> " + hint);
        return hintWord + "\n" + hint;
    }

    private void validateWord(String word) throws GameException {

        if (word.length() != WORD_LENGTH || !word.chars().allMatch(Character::isLetter)) {
            throw new InvalidWordFormatException(word);
        }
        if (!dictionary.contains(word)) {
            throw new WordNotFoundInDictionary(word);
        }
        if (usedWords.contains(word)) {
            throw new GameException("Слово уже использовалось");
        }
    }

    private void updateKnownPositions(String word, String hint) {

        for (int position = 0; position < WORD_LENGTH; position++) {
            if (hint.charAt(position) == '+') {
                knownPosition.put(position, word.charAt(position));
            }
        }
    }

    private String findOnePlusHintWord() throws GameException {

        String best = null;

        for (String candidate : dictionary.getWords()) {

            if (usedWords.contains(candidate) || candidate.equals(answer)) {
                continue;
            }

            String hint = WordleDictionary.analyze(candidate, answer);

            int newPluses = 0;
            boolean invalid = false;

            for (int position = 0; position < WORD_LENGTH; position++) {
                if (hint.charAt(position) == '+') {
                    if (knownPosition.containsKey(position)) {
                        invalid = true;
                        break;
                    }
                    newPluses++;
                }
            }
            if (!invalid && newPluses == 1) {
                best = candidate;
                break;
            }
        }
        if (best == null) {
            throw new GameException("Нет доступной подсказки");
        }

        return best;
    }

    boolean isAwaitingFinalHint() {
        return awaitingFinalHint;
    }
}
