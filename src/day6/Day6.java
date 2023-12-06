package day6;

public class Day6 {
    static int[] times = {54, 70, 82, 75};
    static int[] distances = {239, 1142, 1295, 1253};

    static long time = 54708275;
    static long distance = 239114212951253L;


    public static void main(String[] args) {
        // Part 1:
        System.out.println(getPart1());
        // Part 2:
        System.out.println(getPart2());
    }

    static int getPart1() {
        int possibleTimes = 1;
        for (int i = 0; i < times.length; i++) {
            int tally = 0;
            for (int speed = 1; speed < times[i]; speed++) {
                int remainingTime = times[i] - speed;
                if (remainingTime * speed > distances[i]) tally++;
            }
            possibleTimes *= tally;
        }
        return possibleTimes;
    }

    static long getPart2() {
        long possibleTimes = 0;
        for (int speed = 1; speed < time; speed++)
            if ((time - speed) * speed > distance)
                possibleTimes++;
        return possibleTimes;
    }
}
