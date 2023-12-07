package day7;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day7 {

    record Hand(String cards, int bid) {
        double getValue() {
            double value = 0;
            int i = 5;
            for (char c : cards.toCharArray()) {
                switch (c) {
                    case 'A' -> value += 13 * Math.pow(15, i);
                    case 'K' -> value += 12 * Math.pow(15, i);
                    case 'Q' -> value += 11 * Math.pow(15, i);
                    case 'J' -> value += 10 * Math.pow(15, i);
                    case 'T' -> value += 9 * Math.pow(15, i);
                    default -> value += ((Integer.parseInt(Character.toString(c))) - 1) * Math.pow(15, i);
                }
                i--;
            }
            return value;
        }

        @Override
        public String toString() {
            return "[" + cards + "]";
        }
    }

    static List<Hand> handStack = new ArrayList<>();
    static List<Hand> fiveKinds = new ArrayList<>();
    static List<Hand> fourKinds = new ArrayList<>();
    static List<Hand> fullHouses = new ArrayList<>();
    static List<Hand> threeKinds = new ArrayList<>();
    static List<Hand> twoPairs = new ArrayList<>();
    static List<Hand> onePairs = new ArrayList<>();
    static List<Hand> highHands = new ArrayList<>();
    static List<Hand> sortedList = new ArrayList<>();

    static void setData(boolean partTwo) throws IOException {
        clearLists();
        List<String> lines = Files.readAllLines(Paths.get("src/day7/input.txt"));
        for (String line : lines) {
            String[] split = line.split(" ");
            if (!partTwo)
                handStack.add(new Hand(split[0], Integer.parseInt(split[1])));
            else
                handStack.add(new Hand(split[0].replaceAll("J", "1"), Integer.parseInt(split[1])));

        }
    }

    private static void clearLists() {
        for (List<Hand> list : List.of(handStack, fiveKinds, fourKinds, fullHouses, threeKinds, twoPairs, onePairs, highHands, sortedList)) {
            list.clear();
        }
    }

    private static void setFirstOrder(boolean partTwo) {
        for (Hand hand : handStack) {
            if (isFiveKind(hand, partTwo)) {
                fiveKinds.add(hand);
            } else if (isFourKind(hand, partTwo)) {
                fourKinds.add(hand);
            } else if (isFullHouse(hand, partTwo)) {
                fullHouses.add(hand);
            } else if (isThreeKind(hand, partTwo)) {
                threeKinds.add(hand);
            } else if (isTwoPair(hand, partTwo)) {
                twoPairs.add(hand);
            } else if (isOnePair(hand, partTwo)) {
                onePairs.add(hand);
            } else if (isHighCard(hand)) {
                highHands.add(hand);
            }
        }
    }

    static int numberJokers(Hand hand) {
        int count = 0;
        for (char c : hand.cards.toCharArray()) {
            if (c == '1') count++;
        }
        return count;
    }

    private static void setSecondOrder() {
        sort(fiveKinds, fourKinds, fullHouses);
        sort(threeKinds, twoPairs, onePairs);
        highHands.sort((o1, o2) -> Double.compare(o2.getValue(), o1.getValue()));
        sortedList.addAll(highHands);
    }

    private static void sort(List<Hand> fiveKinds, List<Hand> fourKinds, List<Hand> fullHouses) {
        fiveKinds.sort((o1, o2) -> Double.compare(o2.getValue(), o1.getValue()));
        sortedList.addAll(fiveKinds);
        fourKinds.sort((o1, o2) -> Double.compare(o2.getValue(), o1.getValue()));
        sortedList.addAll(fourKinds);
        fullHouses.sort((o1, o2) -> Double.compare(o2.getValue(), o1.getValue()));
        sortedList.addAll(fullHouses);
    }

    public static long getScore() {
        int result = 0;
        for (int i = 0; i < sortedList.size(); i++) {
            result += sortedList.get(i).bid * (i + 1);
        }
        return result;
    }

    private static boolean isFiveKind(Hand hand, boolean partTwo) {
        if (!partTwo)
            return hand.cards.chars().distinct().count() == 1;
        return hand.cards.chars().distinct().count() == 1 || (sameCards(hand) + numberJokers(hand) == 5);
    }

    private static boolean isFourKind(Hand hand, boolean partTwo) {
        if (!partTwo)
            return sameCards(hand) == 4;
        return sameCards(hand) == 4 || (sameCards(hand) + numberJokers(hand) == 4);
    }

    private static boolean isFullHouse(Hand hand, boolean partTwo) {
        if (!partTwo)
            return sameCards(hand) == 3 && hand.cards.chars().distinct().count() == 2;
        return isFullHouse(hand, false) ||
                isTwoPair(hand, false) && numberJokers(hand) == 1;

    }

    private static boolean isThreeKind(Hand hand, boolean partTwo) {
        if (!partTwo)
            return sameCards(hand) == 3 && !isFullHouse(hand, false);
        return sameCards(hand) == 3 || sameCards(hand) + numberJokers(hand) == 3;
    }

    private static boolean isTwoPair(Hand hand, boolean partTwo) {
        if (!partTwo)
            return sameCards(hand) == 2 && hand.cards.chars().distinct().count() == 3;
        return (sameCards(hand) == 2 && hand.cards.chars().distinct().count() == 3) || sameCards(hand) == 2 && numberJokers(hand) == 1;
    }

    private static boolean isOnePair(Hand hand, boolean partTwo) {
        if (!partTwo)
            return sameCards(hand) == 2 && hand.cards.chars().distinct().count() == 4;
        return (sameCards(hand) == 2 && hand.cards.chars().distinct().count() == 4) || isHighCard(hand) && numberJokers(hand) == 1;
    }


    private static boolean isHighCard(Hand hand) {
        return hand.cards.chars().distinct().count() == 5;
    }


    private static int sameCards(Hand hand) {
        String toCount = hand.cards.replaceAll("1", "");
        int maxTally = 1;
        for (int i = 0; i < toCount.length() - 1; i++) {
            int tally = 1;
            char value = toCount.charAt(i);
            for (int j = i + 1; j < toCount.length(); j++) {
                if (toCount.charAt(j) == value) {
                    tally++;
                }
            }
            maxTally = Math.max(maxTally, tally);
        }
        return maxTally;
    }

    private static void getResult(boolean isPartTwo) {
        try {
            setData(isPartTwo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setFirstOrder(isPartTwo);
        setSecondOrder();
        Collections.reverse(sortedList);
        System.out.println(getScore());
    }

    public static void main(String[] args) {
        getResult(false);
        getResult(true);
    }
}
