package day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day9 {

    static List<List<Integer>> valueHistories = new ArrayList<>();

    static void setData() {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get("src/day9/input.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String line : lines) {
            List<Integer> values = new ArrayList<>();
            for (String number : line.split(" ")) {
                values.add(Integer.parseInt(number));
            }
            valueHistories.add(values);
        }
    }

    static int calculateNext(List<Integer> values) {
        List<List<Integer>> extrapolations = new ArrayList<>();
        boolean finished = false;
        List<Integer> current = values;
        while (!finished) {
            List<Integer> difference = new ArrayList<>();
            boolean allZeros = true;
            for (int i = 0; i < current.size() - 1; i++) {
                int diff = current.get(i + 1) - current.get(i);
                difference.add(diff);
                allZeros &= diff == 0;
            }
            extrapolations.add(difference);
            finished = allZeros;
            current = difference;
        }
        int result = 0;
        extrapolations.add(values);
        for (List<Integer> list : extrapolations) {
            result += list.get(list.size() - 1);
        }
        return result;
    }

    static int getResult(boolean partTwo) {
        int result = 0;
        for (List<Integer> values : valueHistories) {
            if (partTwo) {
                Collections.reverse(values);
            }
            result += calculateNext(values);
        }
        return result;
    }

    public static void main(String[] args) {
        setData();
        // Part one
        System.out.println(getResult(false));
        // Part two
        System.out.println(getResult(true));

    }
}
