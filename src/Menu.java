/**
 * Fichier : Menu.java
 * Responsabilité : Gérer l'interface graphique du menu principal avec différentes options de jeu et un bouton pour basculer entre le mode sombre et clair.
 * Date de modification : 30 Mars 2025
 * Auteur(s) : Tristan Baliteau
 */

 import javafx.application.Application;
 import javafx.geometry.Pos;
 import javafx.scene.Scene;
 import javafx.scene.control.Button;
 import javafx.scene.control.Label;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 
 public class Menu extends Application {
     private boolean modeSombre = false;
     private Button toggleModeButton;
     private VBox card;
     private Label title;
     private Scene scene;
 
     /**
      * Méthode principale pour configurer et afficher l'interface graphique du menu principal.
      * Cette méthode crée les boutons et gère la logique d'affichage, ainsi que le changement de taille de la fenêtre.
      * 
      * @param primaryStage Le stage principal de l'application
      */
     @Override
     public void start(Stage primaryStage) {
         primaryStage.setMinWidth(400); 
         primaryStage.setMinHeight(500); 
 
         // Création des boutons via UIHelper
         Button mode1Joueur = UIHelper.createButton("Mode 1 Joueur", "button", e -> lancerMode1Joueur(primaryStage));
         Button mode2Joueurs = UIHelper.createButton("Mode 2 Joueurs", "button", e -> lancerMode2Joueurs(primaryStage));
         Button modePersonnalise = UIHelper.createButton("Mode Personnalisé", "button", e -> lancerModePersonnalise(primaryStage));
         Button quitter = UIHelper.createButton("Quitter", "button", e -> primaryStage.close());
         toggleModeButton = UIHelper.createButton("🌙  Mode Sombre", "button", e -> basculerMode());
 
         // Titre du menu
         title = new Label("Menu Principal");
         title.getStyleClass().add("title");
 
         // Conteneur principal
         card = new VBox(15, title, mode1Joueur, mode2Joueurs, modePersonnalise, quitter, toggleModeButton);
         card.setAlignment(Pos.CENTER);
         card.getStyleClass().add("card");
 
         // Conteneur racine
         VBox root = new VBox(card);
         root.setAlignment(Pos.CENTER);
         root.getStyleClass().add("root");
 
         // Définir la scène
         scene = new Scene(root, 400, 500);
         scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
 
         // Écoute les changements de taille de la fenêtre et ajuste la taille de la VBox
         primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
             double adjustedWidth = newWidth.doubleValue() * 0.8; // Ajuste la largeur à 80% de la largeur actuelle
             double maxWidth = 600; 
             adjustedWidth = Math.min(adjustedWidth, maxWidth); 
             card.setMinWidth(adjustedWidth);
             card.setMaxWidth(adjustedWidth);
         });
 
         primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
             double adjustedHeight = newHeight.doubleValue() * 0.8; // Ajuste la hauteur à 80% de la hauteur actuelle
             double maxHeight = 500; 
             adjustedHeight = Math.min(adjustedHeight, maxHeight); 
             card.setMinHeight(adjustedHeight);
             card.setMaxHeight(adjustedHeight);
         });
 
         // Configure la fenêtre
         primaryStage.setScene(scene);
         primaryStage.setTitle("Menu Principal");
         primaryStage.show();
     }
 
     /**
      * Lance le mode 1 joueur en ouvrant une nouvelle fenêtre.
      * 
      * @param primaryStage Le stage principal de l'application
      */
     private void lancerMode1Joueur(Stage primaryStage) {
         double width = primaryStage.getWidth();
         double height = primaryStage.getHeight();
 
         MotLePlusLong motLePlusLong = new MotLePlusLong();
         Stage newStage = new Stage();
         motLePlusLong.start(newStage);
 
         newStage.setWidth(width);
         newStage.setHeight(height);
 
         primaryStage.close();
     }
 
     /**
      * Lance le mode 2 joueurs en ouvrant une nouvelle fenêtre.
      * 
      * @param primaryStage Le stage principal de l'application
      */
     private void lancerMode2Joueurs(Stage primaryStage) {
         double width = primaryStage.getWidth();
         double height = primaryStage.getHeight();
 
         Mode2Joueurs mode2Joueurs = new Mode2Joueurs();
         Stage newStage = new Stage();
         mode2Joueurs.start(newStage);
 
         newStage.setWidth(width);
         newStage.setHeight(height);
 
         primaryStage.close();
     }
 
     /**
      * Lance le mode personnalisé en ouvrant une nouvelle fenêtre.
      * 
      * @param primaryStage Le stage principal de l'application
      */
     private void lancerModePersonnalise(Stage primaryStage) {
         double width = primaryStage.getWidth();
         double height = primaryStage.getHeight();
 
         ModePersonnalise modePersonnalise = new ModePersonnalise(); 
         Stage newStage = new Stage();
         modePersonnalise.start(newStage);
 
         newStage.setWidth(width);
         newStage.setHeight(height);
 
         primaryStage.close();
     }
 
     /**
      * Bascule entre le mode sombre et clair. Modifie l'apparence de l'interface graphique.
      */
     private void basculerMode() {
         modeSombre = !modeSombre;
         toggleModeButton.setText(modeSombre ? "☀️  Mode Clair" : "🌙  Mode Sombre");
 
         if (modeSombre) {
             scene.getRoot().getStyleClass().add("dark-mode");
         } else {
             scene.getRoot().getStyleClass().remove("dark-mode");
         }
     }
 
     /**
      * Méthode principale pour démarrer l'application JavaFX.
      * 
      * @param args Les arguments de la ligne de commande
      */
     public static void main(String[] args) {
         launch(args);
     }
 }
 