package day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {

    private static List<String> inputList;

    public static void main(String[] args) throws IOException {
        inputList = Files.readAllLines(Paths.get("src/day3/input.txt"));

        // Part 1
        System.out.println(result1());

        // Part 2
        System.out.println(getGearRatio(getGearArray()));


    }

    private static int result1() {
        List<IntPair> potentialParts = extractNumbers(getArray());
        int num = 0;
        for (IntPair part : potentialParts) {
            num += part.number;
        }
        return num;
    }

    public record IntPair(Integer number, String id) {
        @Override
        public String toString() {
            return number + "";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IntPair intPair = (IntPair) o;
            return this.id.equals(intPair.id);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    private static IntPair[][] getArray() {
        Pattern numberPattern = Pattern.compile("\\b\\d+\\b");
        Pattern partMatcher = Pattern.compile("[^\\d.\\s]");
        IntPair[][] grid = new IntPair[inputList.size()][inputList.get(0).length()];
        int row = 0;
        for (String line : inputList) {
            prepNumbers(numberPattern, grid, row, line);
            Matcher symbolMatcher = partMatcher.matcher(line);
            while (symbolMatcher.find()) {
                grid[row][symbolMatcher.start()] = new IntPair(-1, "part");
            }
            row++;
        }
        return grid;
    }

    private static IntPair[][] getGearArray() {
        Pattern numberPattern = Pattern.compile("\\b\\d+\\b");
        Pattern partMatcher = Pattern.compile("\\*");
        IntPair[][] grid = new IntPair[inputList.size()][inputList.get(0).length()];
        int row = 0;
        for (String line : inputList) {
            prepNumbers(numberPattern, grid, row, line);
            Matcher symbolMatcher = partMatcher.matcher(line);

            while (symbolMatcher.find()) {
                grid[row][symbolMatcher.start()] = new IntPair(-1, "gear");
            }
            row++;
        }
        return grid;
    }

    // For each number, that number replaces all its digits by its own value, so that parts that are adjacent to digits can be mapped to the whole number
    private static void prepNumbers(Pattern numberPattern, IntPair[][] grid, int row, String line) {
        Matcher numberMatcher = numberPattern.matcher(line);
        while (numberMatcher.find()) {
            int number = Integer.parseInt(numberMatcher.group());
            int start = numberMatcher.start();
            // create unique id to not add the same number twice
            IntPair intPair = new IntPair(number, row * row + " " + start * Math.random());
            grid[row][numberMatcher.start()] = intPair;
            if (intPair.number / 10 != 0)
                grid[row][numberMatcher.start() + 1] = intPair;
            if (intPair.number / 100 != 0)
                grid[row][numberMatcher.start() + 2] = intPair;
        }
    }

    private static List<IntPair> extractNumbers(IntPair[][] grid) {
        List<IntPair> enginePartSet = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != null && !grid[i][j].id.equals("part")) {
                    for (int x = i - 1; x <= i + 1; x++) {
                        for (int y = j - 1; y <= j + 1; y++) {
                            if (x >= 0 && x < grid.length && y >= 0 && y < grid[i].length && grid[x][y] != null && grid[x][y].id.equals("part")) {
                                if (!enginePartSet.contains(grid[i][j]))
                                    enginePartSet.add(grid[i][j]);
                            }
                        }
                    }
                }
            }
        }
        return enginePartSet;
    }

    private static int getGearRatio(IntPair[][] grid) {
        int result = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != null && grid[i][j].id.equals("gear")) {
                    int numberOfParts = 0;
                    IntPair first = new IntPair(-1, "");
                    IntPair second = new IntPair(-1, "");
                    for (int x = i - 1; x <= i + 1; x++) {
                        for (int y = j - 1; y <= j + 1; y++) {
                            if (x >= 0 && x < grid.length && y >= 0 && y < grid[i].length && grid[x][y] != null && grid[x][y].number > 0) {
                                if (first.number == -1) {
                                    first = grid[x][y];
                                    numberOfParts++;
                                } else if (second.number == -1 && !grid[x][y].equals(first)) {
                                    second = grid[x][y];
                                    numberOfParts++;
                                }
                                if (numberOfParts == 2) {
                                    result += first.number * second.number;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
