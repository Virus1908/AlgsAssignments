import edu.princeton.cs.algs4.Stack;

import java.util.TreeSet;

public class BoggleSolver {
    private final Trie dictionary;

    public BoggleSolver(String[] dictionary) {
        this.dictionary = new Trie();
        for (String s : dictionary) {
            this.dictionary.add(s);
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        CardStack cardStack = new CardStack();
        TreeSet<String> validWords = new TreeSet<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                addAllWordsFromStart(i, j, board, validWords, cardStack);
            }
        }
        return validWords;
    }

    private void addAllWordsFromStart(int i, int j, BoggleBoard board, TreeSet<String> validWords, CardStack cardStack) {
        cardStack.addCard(i, j, board.getLetter(i, j));
        addAllValidWords(cardStack, validWords, board);
        cardStack.pop();
    }

    private void addAllValidWords(CardStack cardStack, TreeSet<String> validWords, BoggleBoard board) {
        String prefix = cardStack.getPrefix();
        if (!dictionary.containsPrefix(prefix)) {
            return;
        }
        if (prefix.length() > 2 && dictionary.containsFull(prefix)) {
            validWords.add(prefix);
        }
        int currentI = cardStack.getCurrentI();
        int currentJ = cardStack.getCurrentJ();
        boolean canMoveUp = currentI != 0;
        boolean canMoveDown = currentI != board.rows() - 1;
        boolean canMoveLeft = currentJ != 0;
        boolean canMoveRight = currentJ != board.cols() - 1;
        if (canMoveUp) {
            tryMove(cardStack, validWords, board, currentI - 1, currentJ);
        }
        if (canMoveUp && canMoveLeft) {
            tryMove(cardStack, validWords, board, currentI - 1, currentJ - 1);
        }
        if (canMoveUp && canMoveRight) {
            tryMove(cardStack, validWords, board, currentI - 1, currentJ + 1);
        }
        if (canMoveDown) {
            tryMove(cardStack, validWords, board, currentI + 1, currentJ);
        }
        if (canMoveDown && canMoveLeft) {
            tryMove(cardStack, validWords, board, currentI + 1, currentJ - 1);
        }
        if (canMoveDown && canMoveRight) {
            tryMove(cardStack, validWords, board, currentI + 1, currentJ + 1);
        }
        if (canMoveLeft) {
            tryMove(cardStack, validWords, board, currentI, currentJ - 1);
        }
        if (canMoveRight) {
            tryMove(cardStack, validWords, board, currentI, currentJ + 1);
        }

    }

    private void tryMove(CardStack cardStack, TreeSet<String> validWords, BoggleBoard board, int i, int j) {
        if (cardStack.isNotVisited(i, j)) {
            cardStack.addCard(i, j, board.getLetter(i, j));
            addAllValidWords(cardStack, validWords, board);
            cardStack.pop();
        }
    }

    private static class CardStack {
        private final Stack<Integer> cards = new Stack<>();
        private final TreeSet<Integer> visitedCards = new TreeSet<>();
        private String value = "";


        void addCard(int i, int j, char boardChar) {
            cards.push(toFingerprint(i, j));
            visitedCards.add(toFingerprint(i, j));
            if (boardChar == 'Q') {
                value = value.concat("QU");
            } else {
                value = value.concat(String.valueOf(boardChar));
            }
        }

        private int toFingerprint(int i, int j) {
            return i * 100 + j;
        }

        String getPrefix() {
            return value;
        }

        void pop() {
            visitedCards.remove(cards.pop());
            if (value.endsWith("QU")) {
                value = value.substring(0, value.length() - 2);
            } else {
                value = value.substring(0, value.length() - 1);
            }
        }

        int getCurrentI() {
            return cards.peek() / 100;
        }

        int getCurrentJ() {
            return cards.peek() % 100;
        }

        boolean isNotVisited(int i, int j) {
            return !visitedCards.contains(toFingerprint(i, j));
        }
    }

    public int scoreOf(String word) {
        if (!dictionary.containsFull(word)) {
            return 0;
        }
        switch (word.length()) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    private static class Trie {
        private static final int R = 32;

        private Node root;

        private static class Node {
            private Node[] next = new Node[R];
            private boolean isString;
        }

        Trie() {
        }

        boolean containsFull(String key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            Node x = get(root, key, 0);
            if (x == null) return false;
            return x.isString;
        }

        boolean containsPrefix(String key) {
            if (key == null) throw new IllegalArgumentException("argument to contains() is null");
            Node x = get(root, key, 0);
            return x != null;
        }

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            char c = key.charAt(d);
            return get(x.next[getIndex(c)], key, d + 1);
        }

        private int getIndex(char c) {
            return c - 'A';
        }

        void add(String key) {
            if (key == null) throw new IllegalArgumentException("argument to add() is null");
            root = add(root, key, 0);
        }

        private Node add(Node x, String key, int d) {
            if (x == null) x = new Node();
            if (d == key.length()) {
                x.isString = true;
            } else {
                char c = key.charAt(d);
                x.next[getIndex(c)] = add(x.next[getIndex(c)], key, d + 1);
            }
            return x;
        }
    }
}
