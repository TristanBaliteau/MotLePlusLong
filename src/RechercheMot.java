/*
 * Fichier : RechercheMot.java
 * Responsabilité : Classe permettant de trouver le mot le plus long possible dans un dictionnaire
 *                  en utilisant une liste de lettres données, via une recherche en profondeur dans un Trie.
 * Date de modification : 30/03/2025
 * Auteur(s) : Tristan Baliteau, Leo Debruyne
 */

 import java.util.List;
 import java.util.Map;
 
 class RechercheMot {
     
     private Dictionnaire dictionnaire;
 
     /**
      * Constructeur de la classe RechercheMot
      * 
      * @param dict Dictionnaire utilisé pour la recherche de mots
      */
     public RechercheMot(Dictionnaire dict) {
         this.dictionnaire = dict;
     }
 
     /**
      * Trouve le mot le plus long possible à partir des lettres données en utilisant un Trie.
      * Effectue une recherche en profondeur sur les mots présents dans le dictionnaire.
      *
      * @param lettres Liste de caractères disponibles pour former des mots
      * @return Le mot le plus long trouvé, ou "Aucun mot trouvé" si aucun mot valide n'a été trouvé
      */
     public String trouverMotLePlusLong(List<Character> lettres) {
         StringBuilder bestWord = new StringBuilder();
         StringBuilder currentWord = new StringBuilder();
         
         // Lance la recherche en profondeur à partir de la racine du Trie
         DFSRecherche(dictionnaire.getTrie().getRacine(), lettres, currentWord, bestWord);
         
         // Retourne le meilleur mot trouvé ou un message indiquant qu'aucun mot n'a été trouvé
         return bestWord.length() > 0 ? bestWord.toString() : "Aucun mot trouvé";
     }
 
     /**
      * Recherche en profondeur (DFS) du mot le plus long possible dans le Trie à partir des lettres données.
      *
      * @param node Noeud actuel du Trie à explorer
      * @param lettres Liste de lettres disponibles pour la construction du mot
      * @param current Mot actuel en construction
      * @param best Meilleur mot trouvé jusqu'à présent
      */
     private void DFSRecherche(TrieNode node, List<Character> lettres, StringBuilder current, StringBuilder best) {
         
         // Si le noeud est un mot complet et que le mot actuel est plus long que le meilleur trouvé, on met à jour
         if (node.estFinMot && current.length() > best.length()) {
             best.setLength(0);  // Réinitialise le meilleur mot
             best.append(current); // Met à jour avec le mot actuel
         }
         
         // Si le mot actuel et les lettres restantes sont moins longs que le meilleur mot, on arrête la recherche
         if (current.length() + lettres.size() <= best.length()) {
             return;
         }
 
         // Parcours des enfants du noeud actuel pour explorer les lettres possibles
         for (Map.Entry<Character, TrieNode> entry : node.enfants.entrySet()) {
             char c = entry.getKey();      // Lettre à explorer
             TrieNode child = entry.getValue(); // Noeud enfant associé à cette lettre
             
             if (lettres.contains(c)) {
                 // Si la lettre est disponible dans la liste, on l'ajoute au mot et on la retire de la liste de lettres
                 lettres.remove((Character) c);
                 current.append(c); // Ajoute la lettre au mot actuel
                 
                 // Appel récursif pour continuer la recherche sur le noeud enfant
                 DFSRecherche(child, lettres, current, best);
                 
                 // Retour en arrière (backtracking) : on enlève la dernière lettre et on remet la lettre dans la liste
                 current.deleteCharAt(current.length() - 1);
                 lettres.add(c);
             }
         }
     }
 }
 