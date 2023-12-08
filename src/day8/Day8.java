package day8;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day8 {

    static List<Node> added = new ArrayList<>();
    static String instructions;
    static List<Triplet> triplets = new ArrayList<>();
    static Triplet rootTriplet;
    static Node rootNode;

    static class Node {
        private final Triplet triplet;
        private Node left;
        private Node right;

        @Override
        public String toString() {
            return triplet.position;
        }

        public Node(Triplet position) {
            this.triplet = position;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public boolean equals(Object n) {
            Node other = (Node) n;
            return this.triplet.position.equals(other.triplet.position);
        }
    }

    record Triplet(String position, String left, String right) {
        @Override
        public String toString() {
            return "(" + position + " , L: " + left + " , R: " + right + ")";
        }
    }

    static void setData(boolean partOne) throws IOException {
        List<String> input = Files.readAllLines(Paths.get("src/day8/input.txt"));
        for (String line : input) {
            if (instructions == null) {
                instructions = line;
            } else {
                line = line.replaceAll(" ", "");
                String[] triples = line.split("([^A-Z0-9]+)");
                if (triples.length == 3) {
                    Triplet current = new Triplet(triples[0], triples[1], triples[2]);
                    if (partOne) {
                        if (current.position.equals("AAA") && rootTriplet == null) rootTriplet = current;
                    }
                    triplets.add(current);
                }
            }
        }
        rootNode = new Node(rootTriplet);
    }

    static Node getNode(Triplet current) {
        for (Node node : added) {
            if (node.triplet.position.equals(current.position)) {
                return node;
            }
        }
        return new Node(current);
    }

    private static Triplet findTriplet(String position) {
        for (Triplet triplet : triplets) {
            if (triplet.position.equals(position)) {
                return triplet;
            }
        }
        return null;
    }

    static void buildGraph(boolean partOne) {
        if (partOne)
            added.add(rootNode);
        for (Triplet current : triplets) {
            Node currentNode = getNode(current);
            Triplet left = findTriplet(current.left);
            Triplet right = findTriplet(current.right);
            if (left.position.equals(right.position)) {
                Node child = getNode(left);
                currentNode.setLeft(child);
                currentNode.setRight(child);
            } else {
                currentNode.setLeft(getNode(left));
                currentNode.setRight(getNode(right));
            }
            if (!added.contains(currentNode)) added.add(currentNode);
            if (!added.contains(currentNode.left)) added.add(currentNode.left);
            if (!added.contains(currentNode.right)) added.add(currentNode.right);
        }

    }

    static int traverseGraph() {
        String position = "AAA";
        Node current = rootNode;
        char[] leftRight = instructions.toCharArray();
        int index = 0;
        int result = 0;
        while (!position.equals("ZZZ")) {
            if (leftRight[index] == 'L') {
                position = current.triplet.left;
                current = current.left;
            } else {
                position = current.triplet.right;
                current = current.right;
            }
            index = (index + 1) % leftRight.length;
            result++;
        }
        return result;
    }

    static List<Node> getStartNodes() {
        List<Node> startingNodes = new ArrayList<>();
        for (Node node : added) {
            if (node.triplet.position.endsWith("A")) {
                startingNodes.add(node);
            }
        }
        return startingNodes;
    }

    static boolean allCycled(boolean[] cycled) {
        for (boolean b : cycled) {
            if (!b) return false;
        }
        return true;
    }

    static long traverseGraph2() {
        List<Node> currentPos = getStartNodes();
        char[] leftRight = instructions.toCharArray();
        int index = 0;

        long[] res = new long[currentPos.size()];
        int size = res.length;
        boolean[] cycledIndices = new boolean[size];
        while (!allCycled(cycledIndices)) {
            for (int i = 0; i < currentPos.size(); i++) {
                Node currentNode = currentPos.get(i);
                if (currentNode.triplet.position.endsWith("Z")) {
                    cycledIndices[i] = true;
                } else if (!cycledIndices[i]) {
                    if (leftRight[index] == 'R') {
                        currentPos.set(i, currentNode.right);
                    } else {
                        currentPos.set(i, currentNode.left);
                    }
                    res[i]++;
                }
            }
            index = (index + 1) % leftRight.length;
        }
        return lcm(res);
    }

    private static long gcd(long a, long b) {
        while (b > 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static long lcm(long a, long b) {
        return a * (b / gcd(a, b));
    }

    private static long lcm(long[] input) {
        long result = input[0];
        for (int i = 1; i < input.length; i++) result = lcm(result, input[i]);
        return result;
    }

    static void printResult(boolean partOne) {
        try {
            setData(partOne);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buildGraph(partOne);
        if (partOne)
            System.out.println(traverseGraph());
        else {
            System.out.println(traverseGraph2());
        }
    }

    public static void main(String[] args) {
        // Part 1:
        printResult(true);
        // Part 2:
        printResult(false);
    }
}
