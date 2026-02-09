package ru.yandex.practicum;

import java.util.*;

public class WordleDictionary {

    private final Set<String> words;

    public WordleDictionary(Set<String> words) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Словарь пуст");
        }
        this.words = words;
    }

    public boolean contains(String word) {
        return words.contains(word);
    }

    public String getRandomWord(Random random) {
        int index = random.nextInt(words.size());
        return new ArrayList<>(words).get(index);
    }

    public Set<String> getWords() {
        return Collections.unmodifiableSet(words);
    }

    public static String normalize(String word) {
        return word.trim().toLowerCase(Locale.ROOT);
    }

    public static String analyze(String guess, String answer) {

        char[] result = new char[5];
        Arrays.fill(result, '-');

        boolean[] answerUsed = new boolean[5];

        for (int position = 0; position < 5; position++) {
            if (guess.charAt(position) == answer.charAt(position)) {
                result[position] = '+';
                answerUsed[position] = true;
            }
        }
        for (int guessPosition = 0; guessPosition < 5; guessPosition++) {
            if (result[guessPosition] == '+') continue;

            for (int answerPosition = 0; answerPosition < 5; answerPosition++) {
                if (!answerUsed[answerPosition] && guess.charAt(guessPosition) == answer.charAt(answerPosition)) {
                    result[guessPosition] = '^';
                    answerUsed[answerPosition] = true;
                    break;
                }
            }
        }

        return  new String(result);
    }
}
