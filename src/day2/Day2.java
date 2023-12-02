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
        return processData((maxGreen, maxBlue, maxRed, gameID) -> maxGreen <= GREEN_THRESHOLD
                && maxBlue <= BLUE_THRESHOLD && maxRed <= RED_THRESHOLD ? gameID : 0);
    }

    public static int getResult2() {
        return processData((maxGreen, maxBlue, maxRed, gameID) -> maxGreen * maxBlue * maxRed);
    }

    private static int processData(TriColorOperation operation) {
        int result = 0;
        try {
            List<String> content = Files.readAllLines(Paths.get("src/day2/input.txt"));
            int gameID = 1;
            for (String entry : content) {
                Tuple<Integer, Integer, Integer> colorTuples = getTuples(entry);
                result += operation.execute(colorTuples.t1(), colorTuples.t2(), colorTuples.t3(), gameID++);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private static Tuple<Integer, Integer, Integer> getTuples(String entry) {
        entry = entry.replaceAll("Game \\d*: ", "");
        StringTokenizer tokenizer = new StringTokenizer(entry, ";");
        int maxGreen = 0, maxBlue = 0, maxRed = 0;
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
        return new Tuple<>(maxGreen, maxBlue, maxRed);
    }

    private interface TriColorOperation {
        int execute(int green, int blue, int red, int gameID);
    }

    private record Tuple<T1, T2, T3>(T1 t1, T2 t2, T3 t3) {
    }
}

