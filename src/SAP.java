import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

public class SAP {
    private final Digraph digraph;

    public SAP(Digraph digraph) {
        this.digraph = new Digraph(digraph);
        if (digraph == null) {
            throw new IllegalArgumentException();
        }
    }

    public int length(int v, int w) {
        if (v < 0 || v >= digraph.V()) {
            throw new IllegalArgumentException();
        }
        if (w < 0 || w >= digraph.V()) {
            throw new IllegalArgumentException();
        }
        int[] distancesToV = findDistances(v);
        int[] distancesToW = findDistances(w);
        int minDist = -1;
        for (int i = 0; i < distancesToV.length; i++) {
            if (distancesToV[i] >= 0 && distancesToW[i] >= 0) {
                if (minDist < 0 || distancesToV[i] + distancesToW[i] < minDist) {
                    minDist = distancesToV[i] + distancesToW[i];
                }
            }

        }
        return minDist;
    }

    public int ancestor(int v, int w) {
        if (v < 0 || v >= digraph.V()) {
            throw new IllegalArgumentException();
        }
        if (w < 0 || w >= digraph.V()) {
            throw new IllegalArgumentException();
        }
        int[] distancesToV = findDistances(v);
        int[] distancesToW = findDistances(w);
        int minDist = Integer.MAX_VALUE;
        int sap = -1;
        for (int i = 0; i < distancesToV.length; i++) {
            if (distancesToV[i] >= 0 && distancesToW[i] >= 0) {
                if (distancesToV[i] + distancesToW[i] < minDist) {
                    minDist = distancesToV[i] + distancesToW[i];
                    sap = i;
                }
            }
        }
        return sap;
    }

    private int[] findDistances(int v) {
        int[] distTo = new int[digraph.V()];
        for (int i = 0; i < distTo.length; i++) {
            distTo[i] = -1;
        }
        distTo[v] = 0;
        Queue<Pair<Integer, Integer>> queue = new Queue<>();
        queue.enqueue(new Pair<>(v, 0));
        while (!queue.isEmpty()) {
            Pair<Integer, Integer> vetr = queue.dequeue();
            for (Integer integer : digraph.adj(vetr.key)) {
                if (distTo[integer] == -1) {
                    queue.enqueue(new Pair<>(integer, vetr.value + 1));
                    distTo[integer] = vetr.value + 1;
                }
            }
        }
        return distTo;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        int[] distancesToV = findDistances(v);
        int[] distancesToW = findDistances(w);
        int minDist = -1;
        for (int i = 0; i < distancesToV.length; i++) {
            if (distancesToV[i] >= 0 && distancesToW[i] >= 0) {
                if (minDist < 0 || distancesToV[i] + distancesToW[i] < minDist) {
                    minDist = distancesToV[i] + distancesToW[i];
                }
            }

        }
        return minDist;

    }

    private int[] findDistances(Iterable<Integer> v) {
        int[] distTo = new int[digraph.V()];
        for (int i = 0; i < distTo.length; i++) {
            distTo[i] = -1;
        }
        for (Integer integer : v) {
            if (integer == null || integer < 0 || integer >= digraph.V()) {
                throw new IllegalArgumentException();
            }
            int[] newDist = findDistances(integer);
            for (int i = 0; i < distTo.length; i++) {
                if (newDist[i] != -1 && (newDist[i] < distTo[i] || distTo[i] == -1))
                    distTo[i] = newDist[i];
            }
        }
        return distTo;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        int[] distancesToV = findDistances(v);
        int[] distancesToW = findDistances(w);
        int minDist = -1;
        int sap = -1;
        for (int i = 0; i < distancesToV.length; i++) {
            if (distancesToV[i] >= 0 && distancesToW[i] >= 0) {
                if (minDist < 0 || distancesToV[i] + distancesToW[i] < minDist) {
                    minDist = distancesToV[i] + distancesToW[i];
                    sap = i;
                }
            }

        }
        return sap;
    }

    private static class Pair<S, V> {
        public final S key;
        public final V value;

        private Pair(S key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
