/**
 * Fichier : Chrono.java
 * Responsabilité : Gérer un chronomètre avec une durée spécifiée et exécuter des actions à chaque seconde écoulée, ainsi qu'à la fin du chrono.
 * Date de modification : 29 Mars 2025
 * Auteur(s) : Tristan Baliteau, Leo Debruyne
 */

 import javafx.animation.KeyFrame;
 import javafx.animation.Timeline;
 import javafx.util.Duration;
 import java.util.function.Consumer;
 
 public class Chrono {
     private int tempsRestant;
     private Timeline timeline;
     private final Consumer<Integer> onUpdate;
     private final Runnable onFinish;
 
     /**
      * Constructeur de la classe Chrono
      * Initialise le chronomètre avec une durée, une fonction de mise à jour et une fonction de fin.
      * 
      * @param dureeEnSecondes La durée du chronomètre en secondes
      * @param onUpdate Callback appelé pour chaque mise à jour du temps restant
      * @param onFinish Callback appelé quand le chronomètre atteint zéro
      */
     public Chrono(int dureeEnSecondes, Consumer<Integer> onUpdate, Runnable onFinish) {
         this.tempsRestant = dureeEnSecondes;
         this.onUpdate = onUpdate;
         this.onFinish = onFinish;
     }
 
     /**
      * Démarre le chronomètre.
      * Le chronomètre se met en marche et appelle la méthode de mise à jour chaque seconde.
      */
     public void demarrer() {
         timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> decrementerTemps()));
         timeline.setCycleCount(Timeline.INDEFINITE);
         timeline.play();
     }
 
     /**
      * Décrémente le temps restant et met à jour l'UI.
      * Arrête le chronomètre lorsque le temps restant est égal à zéro et appelle la méthode de fin.
      */
     private void decrementerTemps() {
         if (tempsRestant > 0) {
             tempsRestant--;
             onUpdate.accept(tempsRestant);
         } else {
             timeline.stop();
             onFinish.run();
         }
     }
 
     /**
      * Arrête le chronomètre manuellement.
      * Si le chronomètre est en cours, il est arrêté immédiatement.
      */
     public void arreter() {
         if (timeline != null) {
             timeline.stop();
         }
     }
 
     /**
      * Récupère le temps restant sur le chronomètre.
      * 
      * @return Le temps restant en secondes
      */
     public int getTempsRestant() {
         return tempsRestant;
     }
 }
 