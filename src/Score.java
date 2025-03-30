/*
 * Fichier : Score.java
 * Responsabilité : Classe permettant de gérer le score d'un joueur, avec des méthodes pour ajouter des points,
 *                  obtenir le score actuel et réinitialiser le score.
 * Date de modification : 30/03/2025
 * Auteur(s) : Tristan Baliteau, Leo Debruyne
 */

 public class Score {

    private int score = 0;

    /**
     * Ajoute un certain nombre de points au score actuel.
     *
     * @param points Nombre de points à ajouter
     * @return Le score mis à jour
     */
    public int ajouter(int points) {
        score += points; 
        return score;    
    }

    /**
     * Obtient le score actuel du joueur.
     *
     * @return Le score actuel
     */
    public int obtenirScore() {
        return score;
    }

    /**
     * Réinitialise le score à zéro.
     */
    public void reinitialiser() {
        score = 0; 
    }
}
