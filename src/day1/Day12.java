package day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 {
    public static void main(String[] args) {
        System.out.println(getResult());
    }

    public static int getResult() {
        int result = 0;
        try {
            for (String word : Files.readAllLines(Paths.get("src/day1/input.txt"))) {
                result += 10 * firstDigit(word) + lastDigit(word);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static int firstDigit(String input) {
        Pattern pattern1 = Pattern.compile("[1-9]");
        Matcher matcher1 = pattern1.matcher(input);
        int index1 =  matcher1.find() ? matcher1.start() : -1;
        Pattern pattern2 = Pattern.compile("one|two|three|four|five|six|seven|eight|nine");
        return extractValue(input, index1, pattern2);
    }

    public static int lastDigit(String input) {
        input = String.valueOf(new StringBuilder(input).reverse());
        Pattern pattern1 = Pattern.compile("[1-9]");
        Matcher matcher1 = pattern1.matcher(input);
        int index1 =  matcher1.find() ? matcher1.start() : -1;
        Pattern pattern2 = Pattern.compile("eno|owt|eerht|ruof|evif|xis|neves|thgie|enin");
        return extractValue(input, index1, pattern2);
    }

    private static int extractValue(String input, int index1, Pattern pattern2) {
        Matcher matcher2 = pattern2.matcher(input);
        int index2 = -1;
        int res = 0;
        if (matcher2.find()) {
            res = getValue(matcher2.group());
            index2 =  matcher2.start();
        }

        if ((index1 >= 0 && index1 < index2) || (index1 >= 0 && index2 < 0)) {
            return Character.getNumericValue(input.charAt(index1));
        } else {
            return res;
        }
    }

    public static int getValue(String word) {
        return switch (word) {
            case "one", "eno" -> 1;
            case "two", "owt" -> 2;
            case "three", "eerht" -> 3;
            case "four", "ruof" -> 4;
            case "five", "evif" -> 5;
            case "six", "xis" -> 6;
            case "seven", "neves" -> 7;
            case "eight", "thgie" -> 8;
            case "nine", "enin" -> 9;

            default -> throw new IllegalStateException(); // Unknown word
        };
    }

}
