/**
 * Fichier : MotLePlusLong.java
 * Responsabilité : Gérer le jeu en mode solo
 * Date de modification : 30 Mars 2025
 * Auteur(s) : Tristan Baliteau, Leo Debruyne
 */

 import javafx.application.Application;
 import javafx.animation.KeyFrame;
 import javafx.animation.Timeline;
 import javafx.scene.Scene;
 import javafx.scene.control.*;
 import javafx.scene.layout.HBox;
 import javafx.scene.layout.VBox;
 import javafx.stage.Stage;
 import javafx.geometry.Pos;
 import javafx.util.Duration;
 import javafx.scene.layout.StackPane;
 import javafx.scene.layout.Pane;
 
 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.Path;
 import java.nio.file.Paths;
 import java.util.*;
 
 public class MotLePlusLong extends Application {
 
     // Déclarations des variables de l'interface
     private Label lettresLabel;
     private TextField motUtilisateurField;
     private Label resultatLabel;
     private Label scoreLabel;
     private Chrono chronometre;
     private List<Character> lettresGenerees;
     private String meilleurMot;
     private Dictionnaire dictionnaire;
     private Score score;
     private Score scoreMax;
     private boolean motSoumis = false;
     private ComboBox<String> dictionnaireDropdown;
     private Set<String> motsValides;
     private boolean modeSombre = false;
     private Button toggleModeButton;
     private Button genererButton;
     private Button verifierButton;
     private Button retourMenuButton;
     private Button melangerButton;
     private Label tempsLabel;
 
     /**
      * Méthode de démarrage de l'application JavaFX.
      * Configure l'interface, les événements et le chronomètre.
      */
     @Override
     public void start(Stage primaryStage) {
         primaryStage.setMinWidth(400); 
         primaryStage.setMinHeight(700); 
 
         lettresLabel = new Label("Lettres : ");
         lettresLabel.getStyleClass().add("text");
 
         motUtilisateurField = new TextField();
         motUtilisateurField.setMaxWidth(250); 
         motUtilisateurField.getStyleClass().add("text-field");
         motUtilisateurField.setPromptText("Entrez un mot");
 
         resultatLabel = new Label();
         resultatLabel.getStyleClass().add("text");
 
         score = new Score();
         scoreLabel = new Label("Score : 0");
         scoreLabel.getStyleClass().add("text");
 
         scoreMax = new Score();
         motsValides = new HashSet<>();
         
         verifierButton = UIHelper.createButton("Vérifier", "button", e -> verifierMot());
         melangerButton = UIHelper.createButton("Mélanger", "button", e -> melangerLettres());
         genererButton = UIHelper.createButton("Générer des lettres", "button", e -> genererLettres());
         toggleModeButton = UIHelper.createButton("🌙  Mode Sombre", "button", e -> basculerMode());
         retourMenuButton = UIHelper.createButton("Retour au Menu", "button", e -> retournerAuMenu(primaryStage));
 
         dictionnaireDropdown = new ComboBox<>();
         dictionnaireDropdown.getStyleClass().add("combo-box");
         dictionnaireDropdown.setMaxWidth(200); 
         dictionnaireDropdown.setMinWidth(200); 
         dictionnaireDropdown.setMaxHeight(30); 
         dictionnaireDropdown.setMinHeight(30); 
         dictionnaireDropdown.setPromptText("Choisir un dictionnaire");
         chargerDictionnaires();
         dictionnaireDropdown.setOnAction(e -> changerDictionnaire(dictionnaireDropdown.getValue()));
 
         tempsLabel = new Label("Temps restant : 5:00");
         tempsLabel.getStyleClass().add("text");
 
         // Layout et ajout à la scène
         VBox layout = creerLayoutUI();
         StackPane root = new StackPane();
         Pane backgroundPane = new Pane();
         root.getChildren().addAll(backgroundPane, layout);
 
         // Scène et affichage
         Scene scene = new Scene(root, 400, 700);
         scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
         primaryStage.setScene(scene);
         primaryStage.setTitle("Mode 2 Joueurs");
         primaryStage.show();

         ajusterTailleFenetreDynamique(primaryStage, layout);
 
         // Sélection du dictionnaire par défaut et démarrage du chronomètre
         selectionnerDictionnaireParDefaut();
         initialiserChronometre();
     }
 
     /**
      * Crée et retourne le layout principal de l'interface utilisateur.
      * Organise les composants dans une disposition verticale.
      */
     private VBox creerLayoutUI() {
         VBox layout = new VBox(15, retourMenuButton, toggleModeButton, dictionnaireDropdown, lettresLabel, motUtilisateurField,
                 genererButton, melangerButton, verifierButton, scoreLabel, tempsLabel, resultatLabel);
         layout.setAlignment(Pos.CENTER);
         layout.getStyleClass().add("card");
         layout.setMinWidth(400); 
         layout.setMinHeight(600); 
         return layout;
     }
 
     /**
      * Ajoute un écouteur pour ajuster dynamiquement la taille de la fenêtre.
      */
     private void ajusterTailleFenetreDynamique(Stage primaryStage, VBox layout) {
         primaryStage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
             double adjustedWidth = newWidth.doubleValue() * 0.8; // Ajuster la largeur à 80% de la nouvelle largeur
             double maxWidth = 600; 
             adjustedWidth = Math.min(adjustedWidth, maxWidth); 
             layout.setMinWidth(adjustedWidth);
             layout.setMaxWidth(adjustedWidth);
         });
 
         primaryStage.heightProperty().addListener((obs, oldHeight, newHeight) -> {
             double adjustedHeight = newHeight.doubleValue() * 0.8; // Ajuster la hauteur à 80% de la nouvelle hauteur
             double maxHeight = 600; 
             adjustedHeight = Math.min(adjustedHeight, maxHeight); 
             layout.setMinHeight(adjustedHeight);
             layout.setMaxHeight(adjustedHeight);
         });
     }
 
     /**
      * Charge les dictionnaires disponibles depuis le répertoire courant.
      * Les dictionnaires sont ajoutés à un ComboBox.
      */
     private void chargerDictionnaires() {
         try {
             List<String> fichiers = new ArrayList<>();
             Files.list(Paths.get(".")).filter(Files::isRegularFile).map(Path::getFileName).map(Path::toString)
                     .filter(name -> name.endsWith(".txt")).forEach(fichiers::add);
             dictionnaireDropdown.getItems().addAll(fichiers);
             if (!fichiers.isEmpty()) {
                 dictionnaireDropdown.setValue(fichiers.get(0));
             }
         } catch (IOException e) {
             System.out.println("Erreur lors du chargement des dictionnaires : " + e.getMessage());
         }
     }
 
     /**
      * Sélectionne le dictionnaire par défaut dans le ComboBox.
      */
     private void selectionnerDictionnaireParDefaut() {
         if (!dictionnaireDropdown.getItems().isEmpty()) {
             changerDictionnaire(dictionnaireDropdown.getItems().get(0));
         }
     }
 
     /**
      * Change le dictionnaire utilisé par l'application.
      * @param fichier Le fichier du dictionnaire à charger.
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
      * Génère un ensemble de lettres aléatoires pour le jeu.
      */
     private void genererLettres() {
         lettresGenerees = GenerateurLettres.genererLettres(8);
         lettresLabel.setText("Lettres : " + lettresGenerees);
         RechercheMot recherche = new RechercheMot(dictionnaire);
         meilleurMot = recherche.trouverMotLePlusLong(lettresGenerees);
         resultatLabel.setText("Essayez de trouver un mot !");
         motsValides.clear();
         motSoumis = false;
     }
 
     /**
      * Vérifie le mot soumis par l'utilisateur.
      * @param motUtilisateur Le mot à vérifier.
      */
     private void verifierMot() {
         if (chronometre.getTempsRestant() <= 0) {
             resultatLabel.setText("Le temps est écoulé ! Vous ne pouvez plus soumettre de mots.");
             return;
         }
 
         if (motSoumis) {
             resultatLabel.setText("Vous avez déjà soumis un mot pour cette génération !");
             return;
         }
 
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
 
         motSoumis = true;
         int points = motUtilisateur.length();
         int pointsMax = meilleurMot.length();
         score.ajouter(points);
         scoreMax.ajouter(pointsMax);
         scoreLabel.setText("Score : " + score.obtenirScore() + " / " + scoreMax.obtenirScore());
 
         if (motUtilisateur.length() == meilleurMot.length()) {
             resultatLabel.setText("Bravo ! Vous avez trouvé le meilleur mot !");
         } else {
             resultatLabel.setText("Mot valide ! Mais le meilleur mot était : " + meilleurMot);
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
      */
     private void retournerAuMenu(Stage primaryStage) {
         new Menu().start(primaryStage);
     }
 
     /**
      * Initialisation du chronomètre.
      */
     private void initialiserChronometre() {
         chronometre = new Chrono(300, 
             this::mettreAJourAffichageTemps, 
             this::finChrono 
         );
         chronometre.demarrer();
     }
 
     /**
      * Met à jour l'affichage du temps restant.
      */
     private void mettreAJourAffichageTemps(int tempsRestant) {
         int minutes = tempsRestant / 60;
         int secondes = tempsRestant % 60;
         tempsLabel.setText(String.format("Temps restant : %02d:%02d", minutes, secondes));
     }
 
     /**
      * Action lorsque le chronomètre est terminé.
      */
     private void finChrono() {
         resultatLabel.setText("Le temps est écoulé ! Partie terminée.");
         genererButton.setDisable(true);
         verifierButton.setDisable(true);
     }
 
     /**
      * Mélange les lettres générées.
      */
     private void melangerLettres() {
         GenerateurLettres.melangerLettres(lettresGenerees);
         lettresLabel.setText("Lettres : " + lettresGenerees);
     }
 
     public static void main(String[] args) {
         launch(args);
     }
 }
 