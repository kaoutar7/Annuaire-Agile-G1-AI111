package fr.eql.ai111.groupe1.annuaire.agile;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import static fr.eql.ai111.groupe1.annuaire.agile.ArbreDansLeRAF.LONGUEUSTAGIARE;

public class Scene extends Application {
    //******************************Cette partie contient les variables +constantes***********

    RandomAccessFile raf;

    {
        try {
            raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static final int NOMSTAGIARE = 20;
    public static final int PRENOMSTAGIARE = 20;
    public static final int DEPARTEMENT = 15;
    public static final int PROMO = 10;

    public static int compteurStag = 0;


    //***************************Cette partie contient toutes les méthodes ******************************


    /**
     * La méthode completer permet de structurer les données qui vont être saisi dans le RAF
     * @param mot : c'est une chaine de caractère : prend nom, prénom, department, promo
     * @param taille : taille de chaque paramètres (nom, prénom, department, promo)
     * @return paramètres (nom, prénom, department, promo) structuré
     */
    public static String completer(String mot, int taille) {

        int nbEspace = taille - mot.length();
        for (int i = 0; i < nbEspace; i++) {
            mot += " ";
        }
        return mot;

    }
    public static String lectureStag2 (int taillechaine, RandomAccessFile raf){

        String chaine = "";



        try {
            for (int i = 0; i < taillechaine; i++){
                chaine += raf.readChar();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return chaine;
    }

    public static void triParOrdreAlpha(int longueurStag, int compteurStag, RandomAccessFile raf){

        try {
            for (int i = 0; i < compteurStag - 1; i++) {
                int indexmin = i;
                raf.seek(i * longueurStag);

                String nomStagMini = "";
                nomStagMini = lectureStag2(NOMSTAGIARE, raf);

                for (int j = i + 1; j < compteurStag; j ++) {
                    raf.seek(j * longueurStag);
                    String nomStagCourant = "";
                    nomStagCourant = lectureStag2(NOMSTAGIARE, raf);


                    if (nomStagCourant.compareTo(nomStagMini) < 0) {
                        indexmin = j;
                        nomStagMini = nomStagCourant;
                    }
                }
                permuter(indexmin * longueurStag, i * longueurStag, raf);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * Cette méthode permet de permuter deux stagiaires
     * @param indexmin
     * @param frontiere
     * @param raf
     */
    private static void permuter(int indexmin, int frontiere, RandomAccessFile raf){


        try {

            int stagChaineTaille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO;

            String stagChaine = "";
            int stagAnnee = 0;

            String stag2Chaine = "";
            int stag2Annee = 0;


            raf.seek(frontiere);
            stagChaine= lectureStag2(stagChaineTaille,raf);
            stagAnnee = raf.readInt();

            raf.seek(indexmin);
            stag2Chaine=lectureStag2(stagChaineTaille,raf);
            stag2Annee = raf.readInt();

            raf.seek(frontiere);
            raf.writeChars(stag2Chaine);
            raf.writeInt(stag2Annee);

            raf.seek(indexmin);
            raf.writeChars(stagChaine);
            raf.writeInt(stagAnnee);




        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public static void createRAF (RandomAccessFile raf){
        String ligne = "";
        String infoStag = "";
        int compteurChoix = 0;

        try {

            //Lecture de fichier.txt
            FileReader fichierOriginal = new FileReader("c:/Projet1/stagiaires.txt");
            BufferedReader bf = new BufferedReader(fichierOriginal);
            while ((ligne = bf.readLine()) != null) {// Tant qu'il y a des lignes dans mon fichier.txt
                if (!ligne.equals("*")) {// si ma ligne != "*" je vais ajouter le stagiaire dans le RAF
                    // sinon j'augmente le nombre des stagiaires

                    for (int i = 0; i < ligne.length(); i++) {

                        if (ligne.charAt(i) != ' ') {
                            infoStag += ligne.charAt(i);

                        }
                    }
                    switch (compteurChoix) {
                        case 0:
                            infoStag = completer(infoStag, NOMSTAGIARE);
                            raf.writeChars(infoStag);

                            break;
                        case 1:
                            infoStag = completer(infoStag, PRENOMSTAGIARE);
                            raf.writeChars(infoStag);

                            break;
                        case 2:
                            infoStag = completer(infoStag, DEPARTEMENT);
                            raf.writeChars(infoStag);

                            break;
                        case 3:
                            infoStag = completer(infoStag, PROMO);
                            raf.writeChars(infoStag);
                            break;
                        case 4:
                            int nbannee = Integer.parseInt(infoStag);
                            raf.writeInt(nbannee);
                            break;
                        default:
                            break;

                    }

                    compteurChoix += 1;


                } else {

                    compteurStag += 1;
                    compteurChoix = 0;
                }

                infoStag = "";

            }

            triParOrdreAlpha(LONGUEUSTAGIARE, compteurStag, raf);




        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    /**
     *cette méthode transforme une ligne qui contient les données d'un stagiaire à un objet stagiaire
     * @param chaine
     * @return objet stagiaire
     */

    public static Stagiaires fabriqueStagiaires(String chaine) {
        Stagiaires stagiaire = null;
        StringTokenizer st = new StringTokenizer(chaine);
        if (st.countTokens() == 5) {
            String nom = st.nextToken();
            String prenom = st.nextToken();
            String departement = st.nextToken();
            String promo = st.nextToken();
            String année = String.valueOf(Integer.valueOf(st.nextToken()));

            stagiaire = new Stagiaires(nom, prenom, departement, promo, année);
        }
        return stagiaire;
    }
    /**
     *cette méthode transforme une ligne qui contient les données d'un stagiaire à un objet stagiaire
     *
     * @return objet stagiaire
     */
    public static List<Stagiaires> lectureStag(int taillechaine, RandomAccessFile raf){
        boolean fileNotFinished = true;
        List<Stagiaires> listStagiaires = new Vector<>();
        String chaine = "";
        while (fileNotFinished){
            chaine ="";
            for (int i = 0; i < taillechaine; i++) {
                try {
                    chaine += raf.readChar();
                }catch (EOFException e){
                    fileNotFinished=false;
                }catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                chaine = chaine  + raf.readInt() ;
            }catch (EOFException e){
                fileNotFinished = false;
            }catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(chaine);
            listStagiaires.add(fabriqueStagiaires(chaine));

        }
        return listStagiaires;
    }

    public static void listeStag (int compteurStag, RandomAccessFile raf){

        try {
            raf.seek(0);
            int taille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT+ PROMO;


            for (int k = 0; k < compteurStag; k++) {
                String chaine = "";
                String chaine1 = lectureStag2(taille, raf);
                chaine = chaine + chaine1 + raf.readInt();
                System.out.println(chaine);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public static List<Stagiaires> rechercheStag(int borneInf, int borneSup, RandomAccessFile raf, String nomStagRecherche) {

        int pivot = 0;
        List<Stagiaires> nomStagActuel;

        try {

            if (borneInf <= borneSup) {
                pivot = (borneInf + borneSup) / 2;
                raf.seek(pivot * LONGUEUSTAGIARE);
                nomStagActuel = lectureStag(65, raf);
                if (nomStagRecherche.compareToIgnoreCase(nomStagActuel.get(0).getNom()) == 0) {
                    raf.seek(pivot * LONGUEUSTAGIARE);
                    System.out.println("Utilisateur trouvé");
                    System.out.println(nomStagActuel.get(0).getNom());
                    System.out.println(nomStagActuel.get(0).getPrenom());
                    System.out.println(nomStagActuel.get(0).getDepartement());
                    System.out.println(nomStagActuel.get(0).getAnnée());
                    System.out.println(nomStagActuel.get(0).getPromo());



                    return nomStagActuel;


                } else {

                    if (nomStagRecherche.compareToIgnoreCase(nomStagActuel.get(0).getNom()) < 0) {
                        return rechercheStag(borneInf, pivot - 1, raf, nomStagRecherche);
                    } else {
                        return rechercheStag(pivot + 1, borneSup, raf, nomStagRecherche);
                    }
                }

            } else {

                System.out.println("\r\n***** Stagiaire introuvable *****");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }



    //***************************Fin partie méthodes***********************************************


    @Override
    public void start(Stage primaryStage) throws Exception {

        //******On va créer le RAF on ouvrant l'appl

        createRAF(raf);

        TableView<Stagiaires> table;
        //Création de la table
        table = new TableView<>();
        table.setPrefSize(700, 490);
        table.setEditable(true);

        //Création de 5 colonnes
        TableColumn<Stagiaires, String> nomCol =
                new TableColumn<Stagiaires, String>("Nom");
        nomCol.setMinWidth(100);

        //Spécifier comment remplir la donnée pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        nomCol.setCellValueFactory(
                new PropertyValueFactory<Stagiaires, String>("nom"));

        TableColumn<Stagiaires, String> prenomCol = new TableColumn<Stagiaires, String>("Prénom");
        nomCol.setMinWidth(100);
        //spécifier une préférence de tri pour cette colonne
        //nomCol.setSortType(TableColumn.SortType.ASCENDING);
        //nomCol.setSortType(TableColumn.SortType.DESCENDING);

        //Spécifier comment remplir la donnée pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        prenomCol.setCellValueFactory(
                new PropertyValueFactory<Stagiaires, String>("prenom"));

        TableColumn<Stagiaires, String> departementCol = new TableColumn<Stagiaires, String>("Département");
        departementCol.setMinWidth(100);
        //Spécifier comment remplir la donnée pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        departementCol.setCellValueFactory(
                new PropertyValueFactory<Stagiaires, String>("departement"));


        TableColumn<Stagiaires, String> promoCol = new TableColumn<Stagiaires, String>("Promotion");
        promoCol.setMinWidth(100);
        //spécifier une préférence de tri pour cette colonne
        //nomCol.setSortType(TableColumn.SortType.ASCENDING);
        //nomCol.setSortType(TableColumn.SortType.DESCENDING);

        //Spécifier comment remplir la donnée pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        promoCol.setCellValueFactory(
                new PropertyValueFactory<Stagiaires, String>("promo"));

        TableColumn<Stagiaires, Integer> annéeCol = new TableColumn<>("Année");
        annéeCol.setMinWidth(100);
        //Spécifier comment remplir la donnée pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        annéeCol.setCellValueFactory(
                new PropertyValueFactory<>("Année"));



        //Creation des zones de textes et boutons

        TextField prenomTextField = new TextField();
        prenomTextField.setPromptText("Prénom");

        TextField nomTextField = new TextField();
        nomTextField.setPromptText("Nom");

        TextField departementTextField = new TextField();
        departementTextField.setPromptText("Département");

        TextField promoTextField = new TextField();
        promoTextField.setPromptText("Promotion");

        TextField annéeTextField = new TextField();
        annéeTextField.setPromptText("Année");

        TextField resultatRechercheTextField = new TextField();
        annéeTextField.setPromptText("Année ");



        Button actualiserButton = new Button("Actualiser");
        Button ajouterButton = new Button("Ajouter");
        Button rechercherButton = new Button("Rechercher");
        Button exporterButton = new Button("Exporter");

        nomTextField.setMaxWidth(170);
        prenomTextField.setMaxWidth(170);
        departementTextField.setMaxWidth(170);
        promoTextField.setMaxWidth(170);
        annéeTextField.setMaxWidth(170);


        // Text fields : Nom , prénom , departement , promo , année
        //  Boutons : trier  / ajouter/ rechercher / exporter



        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(nomTextField,prenomTextField,departementTextField,promoTextField,annéeTextField);

        // Ajouter les items ci dessus dans une Hbox

        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(10,10,10,10));
        vBox1.setSpacing(10);
        vBox1.getChildren().addAll(actualiserButton,ajouterButton,rechercherButton,exporterButton);


        //On ajoute les cinq colonnes à la table
        table.getColumns().addAll(nomCol, prenomCol,departementCol,promoCol,annéeCol);

        //Instanciation de la classe LesStagiaires

        LesStagiaires lesStagiaires = new LesStagiaires("C:\\Projet1\\listeStagiaire.txt");

        // Creer une liste observable des Stagiaires
        RandomAccessFile raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");
        ObservableList<Stagiaires> list = FXCollections.observableArrayList(lectureStag(65,raf));

        // Ajout de la liste au tableau "table"

        table.setItems(list);

        // MenuBar
        MenuBar menuBar = new MenuBar();

        // menus
        Menu connexionMenu = new Menu("Connexion");
        Menu aideMenu = new Menu("Aide");

        // MenuItems pour les menus ci dessus

        MenuItem superAdminMenuItem = new MenuItem("super-administrateur");
        MenuItem administrateurMenuItem = new MenuItem("administrateur");
        MenuItem documentationUtilisateurMenuItem = new MenuItem("documentation utilisateur");
        MenuItem quitterItem = new MenuItem("Quitter");
        // Spécifier un raccourci clavier au menuItem Quitter.

        quitterItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));


        //SeparatorMenuItem.

        SeparatorMenuItem separator= new SeparatorMenuItem();

        // Ajouter les menuItems aux Menus
        connexionMenu.getItems().addAll(superAdminMenuItem,administrateurMenuItem,quitterItem);
        aideMenu.getItems().addAll(documentationUtilisateurMenuItem);


        // Ajouter les Menus au MenuBar
        menuBar.getMenus().addAll(connexionMenu,aideMenu);

        Button menuPrincipaleButton = new Button();


        VBox vbox2 = new VBox(table);



        //*****************************Cette partie pour les actions sur les boutons********************

        // Gestion du click sur le menuItem Quitter.

        quitterItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });


       /* administrateurMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene_loginAdministrateur scene= new Scene_loginAdministrateur();
                try {
                    scene.start(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });*/

        superAdminMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene_login scene_login = new Scene_login();
                try {
                    scene_login.start(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        /**
         * Rihab
         */


        exporterButton.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {

                getHostServices().showDocument("C:\\Projet1\\Annuaire.pdf");


            }
        });

        documentationUtilisateurMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                getHostServices().showDocument("C:\\Projet1\\javadoc\\index.html");



            }
        });



        rechercherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nomAchercher = nomTextField.getText();
                RandomAccessFile raf = null;
                try {
                    raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                List<Stagiaires> StagiaireTrouve = rechercheStag(0, 1317, raf, nomAchercher);
                table.getItems().clear();
                table.setItems((FXCollections.observableArrayList(StagiaireTrouve)));
            }


        });


        /**
         * Rihab
         */

        ajouterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nom =nomTextField.getText();
                String prenom=prenomTextField.getText();
                String departement =departementTextField.getText();
                String promo = promoTextField.getText();
                int annee = Integer.parseInt(annéeTextField.getText());

                Stagiaires contact = new Stagiaires(prenomTextField.getText(),nomTextField.getText(),
                        departementTextField.getText(),
                        promoTextField.getText()
                        ,annéeTextField.getText());

                try {
                    RandomAccessFile rafAjout = new RandomAccessFile("c:/Projet1/listeStagiaireAjout.bin", "rw");
                    rafAjout.setLength(0);
                    int taille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO;
                    raf.seek(0);

                    for (int i = 0; i < compteurStag; i++){
                        String chaine = "";
                        String chaine1 = lectureStag2(taille, raf);
                        int nbannee= raf.readInt();
                        chaine = chaine + chaine1 + nbannee;

                        rafAjout.writeChars(chaine1);
                        rafAjout.writeInt(nbannee);


                    }
                    rafAjout.seek(rafAjout.length());

                    String nomAdded = completer(nom, NOMSTAGIARE);
                    rafAjout.writeChars(nomAdded);

                    String prenomAdded = completer(prenom, PRENOMSTAGIARE);
                    rafAjout.writeChars(prenomAdded);

                    String departAdded = completer(departement, DEPARTEMENT);
                    rafAjout.writeChars(departAdded);

                    String promoAdded = completer(promo, PROMO);
                    rafAjout.writeChars(promoAdded);

                    rafAjout.writeInt(annee);

                    compteurStag = compteurStag+1;

                    triParOrdreAlpha(LONGUEUSTAGIARE, compteurStag, rafAjout);
                    System.out.println("la liste dans le raf Ajout");
                    listeStag(compteurStag,rafAjout);

                    //Avant cette partie tout marche bien avec rafAjout

                    raf.setLength(0);
                    rafAjout.seek(0);
                    for (int j = 0; j < compteurStag; j++){

                        String infostag = lectureStag2(taille,rafAjout);
                        int nb = rafAjout.readInt();

                        raf.writeChars(infostag);
                        raf.writeInt(nb);

                    }

                    System.out.println("liste des stagiaires dans le raf");
                    listeStag(compteurStag, raf);

                    //ObservableList<Stagiaires>list = FXCollections.observableArrayList(lectureStag(65,raf));

                    // Ajout de la liste au tableau "table"

                    //table.setItems(list);







                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //table.getItems().add(contact);



                prenomTextField.clear();
                nomTextField.clear();
                departementTextField.clear();
                promoTextField.clear();
                annéeTextField.clear();
            }
        });
        actualiserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Il faut mettre à jour la liste sur laquelle la table pointe
                //il faut lire de nouveau le raf puis l'afficher dans la table




                // Creer une liste observable des Stagiaires
                RandomAccessFile raf;
                try {
                    raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                list.setAll(lectureStag(65,raf));
                table.setItems(list);
                System.out.println("je suis là");







            }
        });


        //******************************Fin partie les actions sur les boutons************************

        BorderPane root = new BorderPane(null,menuBar,vBox1,hBox,vbox2);
        root.setTop(menuBar);
        root.setLeft(vbox2);
        root.setBottom(hBox);
        root.setAlignment(hBox, Pos.BOTTOM_CENTER);
        root.setRight(vBox1);
        vBox1.setPadding(new Insets(15));
        vBox1.setSpacing(10);
        hBox.setPadding(new Insets(40));
        root.setMargin(vbox2,new Insets(18));




        javafx.scene.Scene scene = new javafx.scene.Scene(root);
        scene.getStylesheets().add(getClass().getResource("stylesheettbdsuperadmin.css").toExternalForm());
        primaryStage.setTitle("Annuaire des Stagiaires");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.setScene(scene);
        primaryStage.show();


    }

    public static void main(String[] args){
        launch(args);

    }
}
