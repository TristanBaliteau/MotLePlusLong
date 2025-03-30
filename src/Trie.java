import java.util.Map;
import java.util.HashMap;

class Trie {
    private TrieNode racine = new TrieNode();

    public void ajouterMot(String mot) {
        TrieNode node = racine;
        for (char c : mot.toCharArray()) {
            node = node.enfants.computeIfAbsent(c, k -> new TrieNode());
        }
        node.estFinMot = true;
    }

    public boolean contientMot(String mot) {
        TrieNode node = racine;
        for (char c : mot.toCharArray()) {
            if (!node.enfants.containsKey(c)) return false;
            node = node.enfants.get(c);
        }
        return node.estFinMot;
    }
    
    public TrieNode getRacine() {
        return racine;
    }
}

