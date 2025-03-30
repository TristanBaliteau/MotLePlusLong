/**
 * Fichier : Dictionnaire.java
 * Responsabilité : Gérer un dictionnaire basé sur un arbre Trie pour ajouter et rechercher des mots.
 * Date de modification : 30 Février 2025
 * Auteur(s) : Tristan Baliteau, Leo Debruyne
 */

 import java.nio.file.Files;
 import java.nio.file.Paths;
 import java.io.IOException;
 import java.util.List;
 
 class Dictionnaire {
     private Trie trie = new Trie();
 
     /**
      * Charge un dictionnaire à partir d'un fichier texte.
      * Chaque ligne du fichier est ajoutée en tant que mot dans le Trie.
      * 
      * @param cheminFichier Le chemin du fichier contenant les mots à charger
      * @throws IOException Si le fichier ne peut pas être lu
      */
     public void chargerDictionnaire(String cheminFichier) throws IOException {
         List<String> lignes = Files.readAllLines(Paths.get(cheminFichier));
         for (String mot : lignes) {
             trie.ajouterMot(mot.trim().toLowerCase());
         }
         System.out.println("Dictionnaire chargé avec " + lignes.size() + " mots.");
     }
 
     /**
      * Vérifie si un mot est présent dans le dictionnaire.
      * Le mot est converti en minuscules avant la recherche.
      * 
      * @param mot Le mot à rechercher
      * @return true si le mot est trouvé, false sinon
      */
     public boolean contientMot(String mot) {
         return trie.contientMot(mot.toLowerCase());
     }
 
     /**
      * Récupère l'instance du Trie utilisée par le dictionnaire.
      * 
      * @return L'instance du Trie
      */
     public Trie getTrie() {
         return trie;
     }
 }
 