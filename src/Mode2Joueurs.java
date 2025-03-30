/**
 * Fichier : Mode2Joueurs.java
 * Responsabilité : Gérer le mode de jeu pour deux joueurs
 * Date de modification : 30 mars 2025
 * Auteur(s) : Tristan Baliteau, Leo Debruyne
 */

 import javafx.application.Application;
 import javafx.geometry.Pos;
 import javafx.scene.Scene;
 import javafx.scene.control.*;
 import javafx.scene.layout.*;
 import javafx.stage.Stage;
 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.Path;
 import java.nio.file.Paths;
 import java.util.*;
 
 public class Mode2Joueurs extends Application {
 
     private Label lettresLabel;
     private TextField motUtilisateurField;
     private Label resultatLabel;
     private Label scoreLabel;
     private Score scoreJoueur1;
     private Score scoreJoueur2;
     private int joueurCourant; 
     private List<Character> lettresGenerees;
     private String meilleurMot;
     private Dictionnaire dictionnaire;
     private ComboBox<String> dictionnaireDropdown;
     private Button toggleModeButton;
     private Button genererButton;
     private Button verifierButton;
     private Button retourMenuButton; 
     private boolean modeSombre = false;
     private Button melangerButton;
     
     /**
      * Initialisation de l'interface utilisateur et des éléments de jeu.
      * Configure la scène, les boutons et les actions associées.
      * @param primaryStage La fenêtre principale de l'application
      */
     @Override
     public void start(Stage primaryStage) {
         primaryStage.setMinWidth(400); 
         primaryStage.setMinHeight(700); 
 
         // Initialisation des éléments de l'interface
         lettresLabel = new Label("Lettres : ");
         lettresLabel.getStyleClass().add("text");
         motUtilisateurField = new TextField();
         motUtilisateurField.setMaxWidth(250); 
         motUtilisateurField.getStyleClass().add("text-field");
         motUtilisateurField.setPromptText("Entrez un mot");
 
         resultatLabel = new Label();
         resultatLabel.getStyleClass().add("text");
         scoreJoueur1 = new Score();
         scoreJoueur2 = new Score();
         scoreLabel = new Label("Joueur 1: 0 | Joueur 2: 0");
         scoreLabel.getStyleClass().add("text");
         joueurCourant = 1; 
 
         // Utilisation de la méthode pour créer les boutons
         genererButton = UIHelper.createButton("Générer des lettres", "button", e -> genererLettres());
         verifierButton = UIHelper.createButton("Vérifier", "button", e -> verifierMot());
         toggleModeButton = UIHelper.createButton("🌙  Mode Sombre", "button", e -> basculerMode());
         retourMenuButton = UIHelper.createButton("Retour au Menu", "button", e -> retournerAuMenu(primaryStage));
         melangerButton = UIHelper.createButton("Mélanger Lettres", "button", e -> melangerLettres());
 
         // ComboBox pour choisir le dictionnaire
         dictionnaireDropdown = new ComboBox<>();
         dictionnaireDropdown.getStyleClass().add("combo-box");
         dictionnaireDropdown.setMaxWidth(200); 
         dictionnaireDropdown.setMinWidth(200); 
         dictionnaireDropdown.setMaxHeight(30); 
         dictionnaireDropdown.setMinHeight(30); 
         dictionnaireDropdown.setPromptText("Choisir un dictionnaire");
         chargerDictionnaires();
         dictionnaireDropdown.setOnAction(e -> changerDictionnaire(dictionnaireDropdown.getValue()));
 
         // Organiser les éléments dans le layout principal
         VBox layout = new VBox(15, retourMenuButton, toggleModeButton, dictionnaireDropdown, lettresLabel, motUtilisateurField,
                 genererButton, melangerButton, verifierButton, scoreLabel, resultatLabel);
         layout.setAlignment(Pos.CENTER);
         layout.getStyleClass().add("card");
         layout.setMinWidth(400); 
         layout.setMinHeight(700); 
 
         // Ajout du fond de carte
         StackPane root = new StackPane();
         Pane backgroundPane = new Pane();
         root.getStyleClass().add("root");
         root.getChildren().addAll(backgroundPane, layout);
 
         // Création de la scène
         Scene scene = new Scene(root, 400, 700);
         scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
         primaryStage.setScene(scene);
         primaryStage.setTitle("Mode 2 Joueurs");
         primaryStage.show();
 
         // Ecouteur de redimensionnement de la fenêtre pour ajuster dynamiquement la taille de la VBox
         primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
             double adjustedWidth = newWidth.doubleValue() * 0.8; // Ajuster la largeur à 80% de la nouvelle largeur
             double maxWidth = 600; 
             adjustedWidth = Math.min(adjustedWidth, maxWidth); 
             layout.setMinWidth(adjustedWidth);
             layout.setMaxWidth(adjustedWidth);
         });
 
         primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
             double adjustedHeight = newHeight.doubleValue() * 0.8; // Ajuster la hauteur à 80% de la nouvelle hauteur
             double maxHeight = 700;
             adjustedHeight = Math.min(adjustedHeight, maxHeight); 
 
             layout.setMinHeight(adjustedHeight);
             layout.setMaxHeight(adjustedHeight);
         });
     }
 
     /**
      * Charger la liste des dictionnaires disponibles depuis le répertoire courant.
      * Met à jour la ComboBox avec les fichiers trouvés.
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
      * Changer le dictionnaire utilisé dans le jeu en fonction du fichier sélectionné.
      * @param fichier Le nom du fichier de dictionnaire à charger
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
      * Générer des lettres aléatoires et afficher ces lettres à l'utilisateur.
      * Calculer également le meilleur mot possible avec ces lettres.
      */
     private void genererLettres() {
         lettresGenerees = GenerateurLettres.genererLettres(8);
         lettresLabel.setText("Lettres : " + lettresGenerees);
         RechercheMot recherche = new RechercheMot(dictionnaire);
         meilleurMot = recherche.trouverMotLePlusLong(lettresGenerees);
         resultatLabel.setText("Joueur " + joueurCourant + ", proposez un mot !");
     }
 
     /**
      * Vérifier la validité du mot proposé par l'utilisateur.
      * Si valide, ajouter les points au joueur courant et passer au joueur suivant.
      */
     private void verifierMot() {
         String motUtilisateur = motUtilisateurField.getText().trim().toLowerCase();
         if (motUtilisateur.isEmpty() || !motUtilisateur.matches("[a-z]+")) {
             resultatLabel.setText("Veuillez entrer un mot valide (a-z).");
             return;
         }
 
         if (!dictionnaire.contientMot(motUtilisateur)) {
             resultatLabel.setText("Mot invalide ! Il n'existe pas dans le dictionnaire.");
             return;
         }
 
         List<Character> lettresDispo = new ArrayList<>(lettresGenerees);
         for (char c : motUtilisateur.toCharArray()) {
             if (!lettresDispo.remove((Character) c)) {
                 resultatLabel.setText("Votre mot utilise des lettres non disponibles !");
                 return;
             }
         }
 
         // Ajouter des points en fonction de la longueur du mot
         int points = motUtilisateur.length();
         if (joueurCourant == 1) {
             scoreJoueur1.ajouter(points);
         } else {
             scoreJoueur2.ajouter(points);
         }
 
         scoreLabel.setText("Joueur 1: " + scoreJoueur1.obtenirScore() + " | Joueur 2: " + scoreJoueur2.obtenirScore());
         motUtilisateurField.clear();
         resultatLabel.setText("Mot validé, joueur " + (joueurCourant == 1 ? 2 : 1) + ", à vous !");
 
         joueurCourant = (joueurCourant == 1) ? 2 : 1;
     }
 
     /**
      * Basculer entre le mode sombre et le mode clair.
      * Met à jour le texte du bouton en conséquence.
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
      * Mélanger les lettres disponibles pour créer un nouvel ensemble de lettres.
      */
     private void melangerLettres() {
         GenerateurLettres.melangerLettres(lettresGenerees);
         lettresLabel.setText("Lettres : " + lettresGenerees);
     }
 
     /**
      * Retourner au menu principal en créant une nouvelle fenêtre.
      * @param primaryStage La fenêtre principale de l'application
      */
     private void retournerAuMenu(Stage primaryStage) {
         new Menu().start(primaryStage);
     }
 
     /**
      * Méthode principale pour lancer l'application.
      * @param args Arguments de la ligne de commande
      */
     public static void main(String[] args) {
         launch(args);
     }
 }
 