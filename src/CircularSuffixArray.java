import edu.princeton.cs.algs4.StdOut;

import java.util.PriorityQueue;

public class CircularSuffixArray {

    private final int[] index;

    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        index = new int[s.length()];
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (int i = 0; i < s.length(); i++) {
            pq.add(new Node(getSuffix(s, i), i));
        }
        for (int i = 0; i < s.length(); i++) {
            Node node = pq.poll();
            index[i] = node.order;
        }
    }

    private static class Node implements Comparable<Node> {
        private final String value;
        private final int order;

        private Node(String value, int order) {
            this.value = value;
            this.order = order;
        }

        @Override
        public int compareTo(Node other) {
            return value.compareTo(other.value);
        }

    }

    private String getSuffix(String s, int i) {
        return s.substring(i) + s.substring(0, i);
    }

    public int length() {
        return index.length;
    }

    public int index(int i) {
        if (i < 0 || i >= length()) {
            throw new IllegalArgumentException();
        }
        return index[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("ABRACADABRA!");
        StdOut.println(circularSuffixArray.length());
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            StdOut.println(circularSuffixArray.index(i));
        }
    }

}
