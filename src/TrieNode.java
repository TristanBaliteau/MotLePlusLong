/*
 * Fichier : TrieNode.java
 * Responsabilité : Classe représentant un nœud dans une structure de trie (arbre préfixe), utilisée pour le stockage et la recherche de mots.
 * Date de modification : 30/03/2025
 * Auteur(s) : Leo Debruyne
 */

 import java.util.Map;
 import java.util.HashMap;
 
 class TrieNode {
 
     // Carte associant chaque caractère à son enfant dans le trie
     Map<Character, TrieNode> enfants = new HashMap<>();
 
     // Indicateur qui signale si le nœud représente la fin d'un mot valide
     boolean estFinMot = false;
 }
 