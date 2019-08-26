import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private final boolean isSolvable;
    private final ArrayList<Board> solution;

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<Node> initialMinPQ = new MinPQ<>();
        initialMinPQ.insert(new Node(initial, 0));
        MinPQ<Node> twinMinPQ = new MinPQ<>();
        twinMinPQ.insert(new Node(initial.twin(), 0));
        while (!initialMinPQ.min().board.isGoal() && !twinMinPQ.min().board.isGoal()) {
            aStarStep(initialMinPQ);
            aStarStep(twinMinPQ);
        }
        isSolvable = initialMinPQ.min().board.isGoal();
        if (isSolvable) {
            solution = new ArrayList<>();
            Node solutionNode = initialMinPQ.min();
            while (!solutionNode.isRoot()) {
                solution.add(solutionNode.board);
                solutionNode = solutionNode.previous;
            }
            solution.add(solutionNode.board);
            Collections.reverse(solution);
        } else {
            solution = null;
        }
    }

    private void aStarStep(MinPQ<Node> minPQ) {
        Node minNode = minPQ.delMin();
        Board prevBoard = minNode.previous != null ? minNode.previous.board : null;
        for (Board neighbor : minNode.board.neighbors()) {
            if (!neighbor.equals(prevBoard)) {
                minPQ.insert(new Node(neighbor, minNode.steps + 1, minNode));
            }
        }
    }

    private static class Node implements Comparable<Node> {
        private final Board board;
        private final int steps;
        private final Node previous;
        private final int priopity;

        private Node(Board board, int steps) {
            this(board, steps, null);
        }

        private Node(Board board, int steps, Node previous) {
            this.board = board;
            this.steps = steps;
            this.previous = previous;
            priopity = board.manhattan();
        }

        boolean isRoot() {
            return previous == null;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.priopity + this.steps, other.priopity + other.steps);
        }
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public int moves() {
        if (solution == null) {
            return -1;
        }
        return solution.size() - 1;
    }

    public Iterable<Board> solution() {
        return solution;
    }
}
