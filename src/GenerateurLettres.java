/**
 * Fichier : GenerateurLettres.java
 * Responsabilité : Générer et mélanger des lettres aléatoires à partir de l'alphabet.
 * Date de modification : 30 Mars 2025
 * Auteur(s) : Tristan Baliteau, Leo Debruyne
 */

 import java.util.List;
 import java.util.ArrayList;
 import java.util.Random;
 import java.util.Collections;
 
 class GenerateurLettres {
     private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
 
     /**
      * Génère une liste de lettres aléatoires de taille n.
      * Chaque lettre est choisie de manière aléatoire dans l'alphabet.
      * 
      * @param n Le nombre de lettres à générer
      * @return Une liste de caractères contenant les lettres générées
      */
     public static List<Character> genererLettres(int n) {
         Random random = new Random();
         List<Character> lettres = new ArrayList<>();
         for (int i = 0; i < n; i++) {
             lettres.add(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
         }
         return lettres;
     }
 
     /**
      * Mélange la liste de lettres donnée de manière aléatoire.
      * 
      * @param lettres La liste de lettres à mélanger
      */
     public static void melangerLettres(List<Character> lettres) {
         Collections.shuffle(lettres);
     }
 }
 