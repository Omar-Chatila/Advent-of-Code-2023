package day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Day5 {
    record Map(long destRangeStart, long sourceRangeStart, long range) {
        @Override
        public String toString() {
            return destRangeStart + "-" + sourceRangeStart + "-" + range;
        }
    }

    record Corresponding(long source, long dest) {
        @Override
        public String toString() {
            return source + "-" + dest;
        }
    }

    static List<List<Map>> sourceToDestList = new ArrayList<>();
    static Long[] nextSeed;// = new ArrayList<>();
    static List<Long> seeds = new ArrayList<>();

    static void setData() throws IOException {
        for (int i = 0; i < 7; i++) {
            sourceToDestList.add(new ArrayList<>());
        }
        List<String> lines = Files.readAllLines(Paths.get("src/day5/input.txt"));
        int index = -1;
        for (String line : lines) {
            if (line.startsWith("seeds")) {
                for (String s : line.substring(line.indexOf(": ") + 2).split("\\s+"))
                    seeds.add(Long.parseLong(s));
            } else {
                if (line.isEmpty()) {
                    index++;
                } else {
                    if (line.matches("[0-9]+\\s[0-9]+\\s[0-9]+")) {
                        String[] cM = line.split("\\s+");
                        sourceToDestList.get(index).add(new Map(Long.parseLong(cM[0]), Long.parseLong(cM[1]), Long.parseLong(cM[2])));
                    }
                }
            }
        }
    }

    private static long getResult() {
        nextSeed = new Long[seeds.size()];
        int j = 0;
        for (Long seed : seeds) {
            nextSeed[j++] = seed;
        }
        for (List<Map> maps : sourceToDestList) {
            for (int i = 0; i < nextSeed.length; i++) {
                long seed = nextSeed[i];
                long nextValue = -1;
                for (Map mapping : maps) {
                    if (seed >= mapping.sourceRangeStart && seed < mapping.sourceRangeStart + mapping.range) {
                        nextValue = seed + (mapping.destRangeStart - mapping.sourceRangeStart);
                        break;
                    } else {
                        nextValue = seed;
                    }
                }
                if (nextValue != -1) {
                    nextSeed[i] = nextValue;
                }
            }
        }
        return Collections.min(Arrays.asList(nextSeed));
    }

    public static void main(String[] args) {
        try {
            setData();
            System.out.println(getResult());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
