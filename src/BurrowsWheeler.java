import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BurrowsWheeler {
    public static void transform() {
        String message = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(message);
        BinaryStdOut.write(getIndexOfZeroIndex(circularSuffixArray));
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            BinaryStdOut.write(getLastCharOfSuffix(message, circularSuffixArray.index(i)));
        }
        BinaryStdOut.close();
    }

    private static int getIndexOfZeroIndex(CircularSuffixArray circularSuffixArray) {
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            if (circularSuffixArray.index(i) == 0) {
                return i;
            }
        }
        throw new IllegalArgumentException();
    }

    private static char getLastCharOfSuffix(String message, int index) {
        if (index == 0) {
            return message.charAt(message.length() - 1);
        }
        return message.charAt(index - 1);
    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String endings = BinaryStdIn.readString();
        int[][] nextAndOrder = createNextArray(endings);
        for (int i = 0; i < endings.length(); i++) {
            BinaryStdOut.write((char) nextAndOrder[1][first]);
            first = nextAndOrder[0][first];
        }
        BinaryStdOut.close();
    }

    private static int[][] createNextArray(String endings) {
        List<List<Integer>> charEndingsHolders = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            charEndingsHolders.add(new LinkedList<>());
        }
        for (int i = 0; i < endings.length(); i++) {
            charEndingsHolders.get(endings.charAt(i)).add(i);
        }
        int[][] nextAndOrder = new int[2][endings.length()];
        int i = 0;
        for (int i1 = 0; i1 < charEndingsHolders.size(); i1++) {
            List<Integer> charEndingsHolder = charEndingsHolders.get(i1);
            if (charEndingsHolder == null) {
                continue;
            }
            for (Object integer : charEndingsHolder) {
                nextAndOrder[0][i] = (int) integer;
                nextAndOrder[1][i] = i1;
                i++;
            }
        }
        return nextAndOrder;
    }

    public static void main(String[] args) {
        if (args != null && args.length == 1) {
            if ("-".equals(args[0])) {
                transform();
            } else if ("+".equals(args[0])) {
                inverseTransform();
            }
        }
    }
}
