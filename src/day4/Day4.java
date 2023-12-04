package day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class Day4 {
    private static List<String> inputList;
    private static List<List<Integer>> winningNumbers;
    private static List<List<Integer>> myNumbers;
    private static int[] wonCards;
    public static void main(String[] args) {
        try {
            inputList = Files.readAllLines(Paths.get("src/day4/input.txt"));
            setLists();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setLists();

        // Part 1
        System.out.println(getScore());
        // Part 2
        System.out.println(sumWonCards());
    }

    private static int sumWonCards() {
        int score = 0;
        for (int number : wonCards) {
            score += number;
        }
        return score;
    }

    public static void setLists() {
        winningNumbers = new ArrayList<>();
        myNumbers = new ArrayList<>();
        for (String line : inputList) {
            line = line.substring(line.indexOf(":") + 1);
            String[] winning = line.split("\\|");
            List<Integer> winningRow = new ArrayList<>();
            List<Integer> myRow = new ArrayList<>();
            StringTokenizer tokenizer = new StringTokenizer(winning[0], " ");
            while (tokenizer.hasMoreTokens()) {
                winningRow.add(Integer.parseInt(tokenizer.nextToken()));
            }
            StringTokenizer tokenizer2 = new StringTokenizer(winning[1], " ");
            while (tokenizer2.hasMoreTokens()) {
                myRow.add(Integer.parseInt(tokenizer2.nextToken()));
            }
            winningNumbers.add(winningRow);
            myNumbers.add(myRow);
        }
    }

    public static int getScore() {
        wonCards = new int[winningNumbers.size()];
        Arrays.fill(wonCards, 1);
        int index = 0;
        int score = 0;
        for (List<Integer> cards : myNumbers) {
            int match = 0;
            for (Integer card : cards) {
                if (winningNumbers.get(index).contains(card)) {
                    match++;
                }
            }
            int i = index + 1;
            for (; i <= index + match; i++) {
                wonCards[i] += wonCards[index];

            }
            score += (int) Math.pow(2, match - 1);
            index++;
        }
        return score;
    }
}
