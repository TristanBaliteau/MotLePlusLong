/*
 * Fichier : UIHelper.java
 * Responsabilité : Classe utilitaire pour créer des boutons stylisés avec un gestionnaire d'événements.
 * Date de modification : 30/03/2025
 * Auteur(s) : Tristan Baliteau
 */

 import javafx.scene.control.Button;
 import javafx.event.ActionEvent;
 import javafx.event.EventHandler;
 
 public class UIHelper {
 
     /*
      * Méthode statique permettant de créer un bouton avec du texte, une classe CSS et un gestionnaire d'événement.
      * @param text Le texte à afficher sur le bouton.
      * @param cssClass La classe CSS à appliquer au bouton.
      * @param action L'action à exécuter lorsqu'on clique sur le bouton.
      * @return Un objet Button configuré avec le texte, la classe CSS et l'action spécifiés.
      */
     public static Button createButton(String text, String cssClass, EventHandler<ActionEvent> action) {
         Button button = new Button(text);
         button.getStyleClass().add(cssClass); 
         button.setOnAction(action); 
         return button; 
     }
 }
 