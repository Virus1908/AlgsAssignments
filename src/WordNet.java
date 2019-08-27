import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class WordNet {
    private final SAP sap;
    private final Map<String, Set<Integer>> nounsToId;
    private final Map<Integer, String> idsToSynsets;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        nounsToId = new HashMap<>();
        idsToSynsets = new HashMap<>();
        In in = new In(synsets);
        String[] line;
        while (!in.isEmpty()) {
            line = in.readLine().split(",");
            for (String noun : line[1].split(" ")) {
                if (!nounsToId.containsKey(noun)) {
                    nounsToId.put(noun, new TreeSet<>());
                }
                nounsToId.get(noun).add(Integer.parseInt(line[0]));
            }
            idsToSynsets.put(Integer.parseInt(line[0]), line[1]);
        }
        Digraph digraph = new Digraph(idsToSynsets.size());
        in = new In(hypernyms);
        int v, w;
        while (!in.isEmpty()) {
            line = in.readLine().split(",");
            if (line.length < 2) {
                continue;
            }
            v = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                w = Integer.parseInt(line[i]);
                digraph.addEdge(v, w);
            }
        }
        Topological topological = new Topological(digraph);
        if (!topological.hasOrder()) {
            throw new IllegalArgumentException();
        } else {
            boolean wasRoot = false;
            for (int i = 0; i < digraph.V(); i++) {
                if (digraph.outdegree(i) == 0) {
                    if (wasRoot) {
                        throw new IllegalArgumentException();
                    }
                    wasRoot = true;
                }
            }
        }
        sap = new SAP(digraph);
    }

    public Iterable<String> nouns() {
        return nounsToId.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounsToId.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return sap.length(nounsToId.get(nounA), nounsToId.get(nounB));
    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return idsToSynsets.get(sap.ancestor(nounsToId.get(nounA), nounsToId.get(nounB)));
    }

}
