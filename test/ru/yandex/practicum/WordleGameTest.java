package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.GameException;
import ru.yandex.practicum.exceptions.InvalidWordFormatException;

import java.io.PrintWriter;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private WordleGame game;
    private WordleDictionary dict;

    @BeforeEach
    void setUp() {
        dict = new WordleDictionary(Set.of("ведро", "весло", "венок", "вилка", "вечно", "ведра", "ветка", "верно"));

        game = new WordleGame(dict, new PrintWriter(System.out), "ведро");
    }


    @Test
    void makeMove_invalidWordLength() {
        assertThrows(InvalidWordFormatException.class, () -> game.makeMove("дом"));
    }

    @Test
    void makeMove_sameWordTwice() throws Exception {
        game.makeMove("весло");

        assertThrows(GameException.class, () -> game.makeMove("весло"));
    }

    @Test
    void game_shouldWinNormally() throws Exception {
        game.makeMove("ведро");

        assertTrue(game.isWin());
        assertFalse(game.isWinByHint());
    }

    @Test
    void suggestWord_shouldWarnBeforeFinalHint() throws Exception {

        game.makeMove("весло");
        game.makeMove("венок");
        game.makeMove("вилка");
        game.makeMove("вечно");
        game.makeMove("ведра");

        String result = game.suggestWord();

        assertTrue(result.contains("Внимание"), "Должно содержать предупреждение при 1 оставшемся шаге");
        assertFalse(game.isWin(), "Игра не должна быть выиграна после предупреждения");
        assertEquals(1, game.getStepsLeft(), "Предупреждение не должно уменьшать количество шагов");
    }

    @Test
    void suggestWord_shouldWinByHintAfterWarning() throws Exception {

        game.makeMove("весло");
        game.makeMove("венок");
        game.makeMove("вилка");
        game.makeMove("вечно");
        game.makeMove("ведра");

        String warning = game.suggestWord();
        assertTrue(warning.contains("Внимание"), "Первый запрос подсказки при 1 шаге должен дать предупреждение");
        assertEquals(1, game.getStepsLeft(), "После предупреждения шаги не должны уменьшиться");

        game.suggestWord();

        assertTrue(game.isWin(), "После финальной подсказки игра должна быть выиграна");
        assertTrue(game.isWinByHint(), "Победа должна быть отмечена как 'нечестная'");
        assertEquals(0, game.getStepsLeft(), "Финальная подсказка должна расходовать последний шаг");
    }

    @Test
    void warningShouldNotConsumeStep() throws Exception {

        game.makeMove("весло");
        game.makeMove("венок");
        game.makeMove("вилка");
        game.makeMove("вечно");
        game.makeMove("ведра");

        int before = game.getStepsLeft();

        String warning = game.suggestWord();
        assertTrue(warning.contains("Внимание"), "Должно быть предупреждение");

        // Шаги не должны уменьшиться после показа предупреждения
        assertEquals(before, game.getStepsLeft(), "Предупреждение не должно расходовать шаг");
        assertTrue(game.isAwaitingFinalHint(), "После предупреждения должен быть установлен флаг awaitingFinalHint");
    }

    private boolean isAwaitingFinalHint(WordleGame game) throws Exception {
        java.lang.reflect.Field field = WordleGame.class.getDeclaredField("awaitingFinalHint");
        field.setAccessible(true);
        return (boolean) field.get(game);
    }
}