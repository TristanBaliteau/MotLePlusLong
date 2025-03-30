/*
 * Fichier : ModePersonnalise.java
 * Responsabilité : Gérer le mode personnalisé du jeu de lettres.
 * Date de modification : 30/03/2025
 * Auteur(s) : Tristan Baliteau, Leo Debruyne
 */

 import javafx.application.Application;
 import javafx.scene.Scene;
 import javafx.scene.control.*;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 import javafx.geometry.Pos;
 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.Path;
 import java.nio.file.Paths;
 import java.util.List;
 import java.util.ArrayList;
 import javafx.scene.layout.StackPane;
 import javafx.scene.layout.Pane;
 
 public class ModePersonnalise extends Application {
 
     private Label lettresLabel;
     private TextField nombreLettresField;
     private TextField lettresUtilisateurField;
     private Button genererButton;
     private Button verifierButton;
     private Label resultatLabel;
     private List<Character> lettresGenerees;
     private Dictionnaire dictionnaire;
     private boolean modeSombre = false;
     private Button toggleModeButton;
     private Button retourMenuButton;
     private ComboBox<String> dictionnaireDropdown; 
 
     @Override
     public void start(Stage primaryStage) {
         primaryStage.setMinWidth(400);
         primaryStage.setMinHeight(700);
 
         // Initialisation des éléments d'interface
         lettresLabel = new Label("Lettres : ");
         lettresLabel.getStyleClass().add("text");
 
         nombreLettresField = new TextField();
         nombreLettresField.setPromptText("Entrez le nombre de lettres");
         nombreLettresField.setMaxWidth(250); 
         nombreLettresField.getStyleClass().add("text-field");
 
         lettresUtilisateurField = new TextField();
         lettresUtilisateurField.setPromptText("Entrez vos lettres (ou laisser vide)");
         lettresUtilisateurField.setMaxWidth(250);
         lettresUtilisateurField.getStyleClass().add("text-field");
 
         // Création des boutons avec leurs actions
         genererButton = UIHelper.createButton("Générer des lettres", "button", e -> genererLettres());
         verifierButton = UIHelper.createButton("Vérifier", "button", e -> verifierMot());
         toggleModeButton = UIHelper.createButton("🌙  Mode Sombre", "button", e -> basculerMode());
         retourMenuButton = UIHelper.createButton("Retour au Menu", "button", e -> retournerAuMenu(primaryStage));
 
         resultatLabel = new Label();
         resultatLabel.getStyleClass().add("text");
 
         // Chargement des dictionnaires
         dictionnaireDropdown = new ComboBox<>();
         dictionnaireDropdown.getStyleClass().add("combo-box");
         dictionnaireDropdown.setPromptText("Choisir un dictionnaire");
         chargerDictionnaires();
         dictionnaireDropdown.setOnAction(e -> changerDictionnaire(dictionnaireDropdown.getValue()));
 
         // Layout principal de la fenêtre
         VBox layout = new VBox(15, retourMenuButton, toggleModeButton, dictionnaireDropdown, lettresLabel, nombreLettresField,
                 genererButton, lettresUtilisateurField, verifierButton, resultatLabel);
         layout.setAlignment(Pos.CENTER);
         layout.getStyleClass().add("card");
         layout.setMinWidth(400);
         layout.setMinHeight(600); 
 
         // Ajout du fond
         StackPane root = new StackPane();
         Pane backgroundPane = new Pane();
         root.getStyleClass().add("root");
         root.getChildren().addAll(backgroundPane, layout);
 
         // Création de la scène
         Scene scene = new Scene(root, 400, 700);
         scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
         primaryStage.setScene(scene);
         primaryStage.setTitle("Mode Personnalisé");
         primaryStage.show();
 
         // Adaptation dynamique de la taille du layout en fonction de la fenêtre
         primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
             double adjustedWidth = newWidth.doubleValue() * 0.8;
             double maxWidth = 600;
             adjustedWidth = Math.min(adjustedWidth, maxWidth);
             layout.setMinWidth(adjustedWidth);
             layout.setMaxWidth(adjustedWidth);
         });
 
         primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
             double adjustedHeight = newHeight.doubleValue() * 0.8;
             double maxHeight = 500;
             adjustedHeight = Math.min(adjustedHeight, maxHeight);
 
             layout.setMinHeight(adjustedHeight);
             layout.setMaxHeight(adjustedHeight);
         });
     }
 
     /**
      * Charge les dictionnaires présents dans le répertoire courant
      * et les ajoute dans le menu déroulant (ComboBox).
      */
     private void chargerDictionnaires() {
         try {
             List<String> fichiers = new ArrayList<>();
             Files.list(Paths.get(".")).filter(Files::isRegularFile).map(Path::getFileName).map(Path::toString)
                     .filter(name -> name.endsWith(".txt")).forEach(fichiers::add);
             dictionnaireDropdown.getItems().addAll(fichiers);
             if (!fichiers.isEmpty()) {
                 dictionnaireDropdown.setValue(fichiers.get(0)); 
                 changerDictionnaire(fichiers.get(0)); 
             }
         } catch (IOException e) {
             System.out.println("Erreur lors du chargement des dictionnaires : " + e.getMessage());
         }
     }
 
     /**
      * Change le dictionnaire utilisé pour vérifier les mots.
      * @param fichier Le nom du fichier dictionnaire à charger.
      */
     private void changerDictionnaire(String fichier) {
         try {
             dictionnaire = new Dictionnaire();
             dictionnaire.chargerDictionnaire(fichier);
             resultatLabel.setText("Dictionnaire chargé : " + fichier);
         } catch (IOException e) {
             resultatLabel.setText("Erreur : Impossible de charger " + fichier);
         }
     }
 
     /**
      * Génère des lettres soit automatiquement en fonction du nombre entré par l'utilisateur,
      * soit à partir des lettres manuellement fournies par l'utilisateur.
      */
     private void genererLettres() {
         String nombreLettresText = nombreLettresField.getText().trim();
 
         if (!nombreLettresText.isEmpty() && nombreLettresText.matches("\\d+")) {
             int nombreLettres = Integer.parseInt(nombreLettresText);
             lettresGenerees = GenerateurLettres.genererLettres(nombreLettres);
             lettresLabel.setText("Lettres générées : " + lettresGenerees);
             lettresUtilisateurField.clear(); 
         } else {
             String lettresUtilisateurText = lettresUtilisateurField.getText().trim().toLowerCase();
             if (lettresUtilisateurText.isEmpty()) {
                 resultatLabel.setText("Veuillez entrer des lettres (ou un nombre valide).");
             } else {
                 lettresGenerees = new ArrayList<>();
                 for (char c : lettresUtilisateurText.toCharArray()) {
                     lettresGenerees.add(c);
                 }
                 lettresLabel.setText("Lettres générées : " + lettresGenerees);
             }
         }
     }
 
     /**
      * Vérifie si un mot peut être formé avec les lettres générées et affiche le mot le plus long possible.
      */
     private void verifierMot() {
         if (lettresGenerees.isEmpty()) {
             resultatLabel.setText("Aucune lettre générée. Veuillez générer des lettres.");
             return;
         }
 
         RechercheMot recherche = new RechercheMot(dictionnaire);
         String meilleurMot = recherche.trouverMotLePlusLong(lettresGenerees);
 
         if (meilleurMot != null && !meilleurMot.isEmpty()) {
             resultatLabel.setText("Le mot le plus long est : " + meilleurMot);
         } else {
             resultatLabel.setText("Aucun mot valide trouvé avec ces lettres.");
         }
     }
 
     /**
      * Basculer entre le mode sombre et le mode clair.
      */
     private void basculerMode() {
         modeSombre = !modeSombre;
         Scene scene = toggleModeButton.getScene();
         toggleModeButton.setText(modeSombre ? "☀️  Mode Clair" : "🌙  Mode Sombre");
 
         if (modeSombre) {
             scene.getRoot().getStyleClass().add("dark-mode");
         } else {
             scene.getRoot().getStyleClass().remove("dark-mode");
         }
     }
 
     /**
      * Retourne au menu principal.
      * @param primaryStage La fenêtre principale.
      */
     private void retournerAuMenu(Stage primaryStage) {
         new Menu().start(primaryStage);
     }
 
     public static void main(String[] args) {
         launch(args);
     }
 }
 