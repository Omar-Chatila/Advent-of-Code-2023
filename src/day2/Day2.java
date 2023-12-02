package day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;

public class Day2 {

    public static final int RED_THRESHOLD = 12;
    public static final int GREEN_THRESHOLD = 13;
    public static final int BLUE_THRESHOLD = 14;

    public static void main(String[] args) {
        // Task 1
        System.out.println(getResult());
        // Task 2
        System.out.println(getResult2());

    }

    public static int getResult() {
        int result = 0;
        try {
            List<String> content = Files.readAllLines(Paths.get("src/day2/input.txt"));
            int gameID = 1;
            for (String entry : content) {
                entry = entry.replaceAll("Game \\d*: ", "");
                StringTokenizer tokenizer = new StringTokenizer(entry, ";");
                int maxGreen = 0, maxRed = 0, maxBlue = 0;
                while (tokenizer.hasMoreElements()) {
                    String set = tokenizer.nextToken();
                    StringTokenizer tokenizer2 = new StringTokenizer(set, ",");
                    while (tokenizer2.hasMoreElements()) {
                        String pair = tokenizer2.nextToken();
                        if (pair.contains("green")) {
                            int green = Integer.parseInt(pair.replaceAll("\\D*", ""));
                            maxGreen = Math.max(green, maxGreen);
                        } else if (pair.contains("blue")) {
                            int blue = Integer.parseInt(pair.replaceAll("\\D*", ""));
                            maxBlue = Math.max(blue, maxBlue);
                        } else {
                            int red = Integer.parseInt(pair.replaceAll("\\D*", ""));
                            maxRed = Math.max(red, maxRed);
                        }
                    }
                }
                if (maxGreen <= GREEN_THRESHOLD && maxBlue <= BLUE_THRESHOLD && maxRed <= RED_THRESHOLD) {
                    result += gameID;
                }
                gameID++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static int getResult2() {
        int result = 0;
        try {
            List<String> content = Files.readAllLines(Paths.get("src/day2/input.txt"));
            for (String entry : content) {
                entry = entry.replaceAll("Game \\d*: ", "");
                StringTokenizer tokenizer = new StringTokenizer(entry, ";");
                int minGreen = 0, minRed = 0, minBlue = 0;
                while (tokenizer.hasMoreElements()) {
                    String set = tokenizer.nextToken();
                    StringTokenizer tokenizer2 = new StringTokenizer(set, ",");
                    while (tokenizer2.hasMoreElements()) {
                        String pair = tokenizer2.nextToken();
                        if (pair.contains("green")) {
                            int green = Integer.parseInt(pair.replaceAll("\\D*", ""));
                            minGreen = Math.max(green, minGreen);
                        } else if (pair.contains("blue")) {
                            int blue = Integer.parseInt(pair.replaceAll("\\D*", ""));
                            minBlue = Math.max(blue, minBlue);
                        } else {
                            int red = Integer.parseInt(pair.replaceAll("\\D*", ""));
                            minRed = Math.max(red, minRed);
                        }
                    }
                }
                int power = minBlue * minRed * minGreen;
                result += power;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
