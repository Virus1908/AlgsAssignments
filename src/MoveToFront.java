import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    private static final int ASCII_COUNT = 256;

    public static void encode() {
        LinkedList<Character> alphabet = new LinkedList<>();
        for (char c = 0; c < ASCII_COUNT; c++) {
            alphabet.add(c);
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            char index = (char) alphabet.indexOf(c);
            BinaryStdOut.write(index);
            alphabet.remove((int) index);
            alphabet.addFirst(c);
        }
        BinaryStdOut.close();
    }

    public static void decode() {
        LinkedList<Character> alphabet = new LinkedList<>();
        for (char c = 0; c < ASCII_COUNT; c++) {
            alphabet.add(c);
        }
        while (!BinaryStdIn.isEmpty()) {
            char index = BinaryStdIn.readChar();
            char c = alphabet.get(index);
            BinaryStdOut.write(c);
            alphabet.remove((int) index);
            alphabet.addFirst(c);
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args != null && args.length == 1) {
            if ("-".equals(args[0])) {
                encode();
            } else if ("+".equals(args[0])) {
                decode();
            }
        }
    }
}
