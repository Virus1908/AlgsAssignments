public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        int maxValue = -1;
        String maxNoun = null;
        for (int i = 0; i < nouns.length; i++) {
            String noun = nouns[i];
            int outcast = distSum(nouns, i);
            if (maxValue < 0 || maxValue < outcast) {
                maxValue = outcast;
                maxNoun = noun;
            }
        }
        return maxNoun;
    }

    private int distSum(String[] nouns, int i) {
        int sum = 0;
        for (String noun : nouns) {
            if (noun.equals(nouns[i])) {
                continue;
            }
            sum += wordnet.distance(noun, nouns[i]);
        }
        return sum;
    }
}
