package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day11 {

    /*
        combining first two digits and adding them
     */

    public static void main(String[] args) {
        System.out.println(getResult());
    }

    private static int getResult() {
        int result = 0;
        try {
            for (String word : Files.readAllLines(Paths.get("src/day1/input.txt"))) {
                String cropped = word.replaceAll("[^0-9]", "");
                result += Character.getNumericValue(cropped.charAt(0)) * 10 +
                        Character.getNumericValue(cropped.charAt(cropped.length() - 1));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
