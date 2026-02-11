package ru.yandex.practicum;

import  ru.yandex.practicum.exceptions.*;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {
        try (
                PrintWriter log = new PrintWriter(new FileWriter("wordle.log", true));
                Scanner scanner = new Scanner(System.in)
        ) {

            log.println("Новая игра Wordle");

            WordleDictionaryLoader loader = new WordleDictionaryLoader(log);
            WordleDictionary dictionary = loader.load("words_ru.txt");

            WordleGame game = new WordleGame(dictionary, log);

            while (!game.isFinished()) {

                System.out.println("У вас осталось: " + game.getStepsLeft() + " ходов.");
                System.out.print("Введите слово(Enter – подсказка): ");

                String input = scanner.nextLine();


                try {
                    String result = input.isBlank()
                            ? game.suggestWord()
                            : game.makeMove(input);

                    System.out.println(result);

                    if (game.isWin()) {
                        if (game.isWinByHint()) {
                            System.out.println("Поздравляем с победой, хоть она и достигнута нечестным путем...");
                        } else {
                            System.out.println("Поздравляю с победой !!!!!");
                        }
                        return;
                    }

                } catch (GameException e) {
                    System.out.println(e.getMessage());
                    log.println("Игровая ошибка: " + e.getMessage());
                }
            }


                System.out.println("К сожалению вы проиграли...");
                System.out.println("Правильный ответ был: " + game.getAnswer());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
