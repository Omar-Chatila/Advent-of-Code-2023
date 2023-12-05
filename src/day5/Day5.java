package day5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Day5 {

    record Map(long destRangeStart, long sourceRangeStart, long range) {
        @Override
        public String toString() {
            return destRangeStart + "-" + sourceRangeStart + "-" + range;
        }
    }

    record Interval(long start, long end) {
        @Override
        public String toString() {
            return "[" + start + ", " + end + "]";
        }
    }

    static List<List<Map>> sourceToDestList = new ArrayList<>();
    static List<Long> seeds = new ArrayList<>();
    static final int maxRangeSize = (int) 1E6;

    static void setData() throws IOException {
        for (int i = 0; i < 7; i++) {
            sourceToDestList.add(new ArrayList<>());
        }
        List<String> lines = Files.readAllLines(Paths.get("src/day5/input.txt"));
        int index = -1;
        for (String line : lines) {
            if (line.startsWith("seeds")) {
                for (String s : line.substring(line.indexOf(": ") + 2).split("\\s+")) {
                    seeds.add(Long.parseLong(s));
                }
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

    static List<Interval> setIntervals(List<Long> numbers) {
        List<Interval> intervals = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i += 2) {
            long start = numbers.get(i);
            long end = start + numbers.get(i + 1);
            if (end - start > Day5.maxRangeSize) {
                for (long subStart = start; subStart < end; subStart += Day5.maxRangeSize) {
                    long subEnd = Math.min(subStart + Day5.maxRangeSize, end);
                    intervals.add(new Interval(subStart, subEnd));
                }
            } else {
                List<Interval> overlappingIntervals = new ArrayList<>();
                for (Interval interval : intervals) {
                    if (!(end < interval.start() || start > interval.end())) {
                        overlappingIntervals.add(interval);
                    }
                }
                for (Interval overlap : overlappingIntervals) {
                    intervals.remove(overlap);
                    start = Math.min(start, overlap.start());
                    end = Math.max(end, overlap.end());
                }
                intervals.add(new Interval(start, end));
            }
        }

        return intervals;
    }

    private static long getSecondResult() throws InterruptedException, ExecutionException {
        List<Interval> intervals = (setIntervals(seeds));
        long min = Long.MAX_VALUE;
        ExecutorService executor = Executors.newFixedThreadPool(intervals.size());
        List<Future<Long>> minCandidates = new ArrayList<>();
        for (Interval interval : intervals) {
            Callable<Long> callable = () -> {
                long innerMin = Long.MAX_VALUE;
                for (long i = interval.start; i < interval.end; i++) {
                    innerMin = Math.min(innerMin, getSingleLocation(i));
                }
                return innerMin;
            };
            Future<Long> future = executor.submit(callable);
            minCandidates.add(future);
        }

        for (Future<Long> future : minCandidates) {
            min = Math.min(min, future.get());
        }
        executor.shutdown();

        return min;
    }

    private static long getFirstResult() {
        long min = Long.MAX_VALUE;
        for (long seed : seeds) {
            min = Math.min(min, getSingleLocation(seed));
        }
        return min;
    }

    static long getSingleLocation(long seed) {
        for (List<Map> maps : sourceToDestList) {
            long nextValue = -1;
            for (Map mapping : maps) {
                if (seed >= mapping.sourceRangeStart && seed < mapping.sourceRangeStart + mapping.range) {
                    nextValue = seed + (mapping.destRangeStart - mapping.sourceRangeStart);
                    break;
                } else {
                    nextValue = seed;
                }
            }
            seed = nextValue;
        }
        return seed;
    }

    public static void main(String[] args) {
        try {
            setData();
            long firstResult = getFirstResult();
            long secondResult;// getSecondResult();
            long start = System.nanoTime();
            secondResult = getSecondResult();
            long time = System.nanoTime() - start;
            System.out.println(time / 1E6 + "ms");
            System.out.println("First part: " + firstResult + "\nSecond part: " + secondResult);
        } catch (IOException | ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
