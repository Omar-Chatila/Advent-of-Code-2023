package day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
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
    static List<List<Corresponding>> sourceDestList = new ArrayList<>();
    static List<Long> seeds = new ArrayList<>();

    static void setData() throws IOException {
        for (int i = 0; i < 7; i++) {
            sourceToDestList.add(new ArrayList<>());
            sourceDestList.add(new ArrayList<>());
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
        int j = 0;
        for (List<Map> maps : sourceToDestList) {
            for (int i = 0; i < 100; i++) {
                for (Map map : maps) {
                    if (i >= map.sourceRangeStart && i < map.sourceRangeStart + map.range) {
                        sourceDestList.get(j).add(new Corresponding(i, i + map.range));
                    }
                }
            }
            sourceDestList.get(j).sort(Comparator.comparingLong(o -> o.source));
            j++;
        }

        for (List<Corresponding> correspondings : sourceDestList) {
            System.out.println(correspondings);
        }

    }

    public static void main(String[] args) {
        try {
            setData();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
