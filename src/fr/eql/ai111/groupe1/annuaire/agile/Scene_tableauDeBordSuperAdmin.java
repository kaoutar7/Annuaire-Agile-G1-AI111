package fr.eql.ai111.groupe1.annuaire.agile;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.Scene;

import javax.print.DocFlavor;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.sun.jmx.snmp.ThreadContext.contains;
import static fr.eql.ai111.groupe1.annuaire.agile.ArbreDansLeRAF.LONGUEUSTAGIARE;
import static java.awt.SystemColor.text;

public class Scene_tableauDeBordSuperAdmin extends Application {
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


    //***************************Cette partie contient toutes les m??thodes ******************************


    /**
     * La m??thode completer permet de structurer les donn??es qui vont ??tre saisi dans le RAF
     * @param mot : c'est une chaine de caract??re : prend nom, pr??nom, department, promo
     * @param taille : taille de chaque param??tres (nom, pr??nom, department, promo)
     * @return param??tres (nom, pr??nom, department, promo) structur??
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
     * Cette m??thode permet de permuter deux stagiaires
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
     *cette m??thode transforme une ligne qui contient les donn??es d'un stagiaire ?? un objet stagiaire
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
            String ann??e = String.valueOf(Integer.valueOf(st.nextToken()));

            stagiaire = new Stagiaires(nom, prenom, departement, promo, ann??e);
        }
        return stagiaire;
    }
    /**
     *cette m??thode transforme une ligne qui contient les donn??es d'un stagiaire ?? un objet stagiaire
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

    public static void rechercheStag2(int borneInf, int borneSup, RandomAccessFile raf, String nomStagRecherche){

        int pivot =0;
        String nomStagActuel ="";

        try {

            if (borneInf <= borneSup){
                pivot = (borneInf + borneSup) / 2;
                raf.seek(pivot* LONGUEUSTAGIARE);
                nomStagActuel =  lectureStag2(NOMSTAGIARE,raf);
                if (nomStagRecherche.compareToIgnoreCase(nomStagActuel) == 0 ){
                    raf.seek(pivot*LONGUEUSTAGIARE);
                    System.out.println("**************\r\n" + " Les infos de stagiaire demand?? :\r\n" +
                            "Nom  : "  + lectureStag2(NOMSTAGIARE, raf) + "\r\n" +
                            "Pr??nom  : " + lectureStag2(PRENOMSTAGIARE, raf) + "\r\n" +
                            "D??partement : " + lectureStag2(DEPARTEMENT, raf) + "\r\n" +
                            "Promo : " + lectureStag2(PROMO, raf) + "\r\n" +
                            "Ann??e : " + raf.readInt() );

                }else {

                    if (nomStagRecherche.compareToIgnoreCase(nomStagActuel) < 0) {
                        rechercheStag2(borneInf, pivot - 1, raf, nomStagRecherche);
                    } else {
                        rechercheStag2(pivot + 1, borneSup, raf, nomStagRecherche);
                    }
                }

            } else {

                System.out.println("\r\n***** Stagiaire introuvable *****");
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
    public String rechercheStagBoutonRecherche(int borneInf, int borneSup, RandomAccessFile raf, String nomStagRecherche){

        int pivot =0;
        String nomStagActuel ="";
        String resultat = "";

        try {

            if (borneInf <= borneSup){
                pivot = (borneInf + borneSup) / 2;
                raf.seek(pivot* LONGUEUSTAGIARE);
                nomStagActuel =  lectureStag2(NOMSTAGIARE,raf);
                if (nomStagRecherche.compareToIgnoreCase(nomStagActuel) == 0 ){
                    raf.seek(pivot*LONGUEUSTAGIARE);

                   /* resultat=
                            "Nom  : "  + lectureStag2(NOMSTAGIARE, raf) + "\r\n" +
                            "Pr??nom  : " + lectureStag2(PRENOMSTAGIARE, raf) + "\r\n" +
                            "D??partement : " + lectureStag2(DEPARTEMENT, raf) + "\r\n" +
                            "Promo : " + lectureStag2(PROMO, raf) + "\r\n" +
                            "Ann??e : " + raf.readInt();


                    System.out.println("nomStagRecherche " + resultat);*/
                    String nom = lectureStag2(NOMSTAGIARE,raf);
                    String prenom = lectureStag2(PRENOMSTAGIARE,raf);
                    String depart = lectureStag2(DEPARTEMENT,raf);
                    String promo = lectureStag2(PROMO,raf);


                    String resultat1= "Nom :            " + nom +"\r\n"+
                            "Pr??nom :             " + prenom +"\r\n"+
                            "D??partement :        " + depart+ "\r\n" +
                            "Promo :                " + promo;
                    resultat = resultat1 +"\r\nAnn??e :                "+ raf.readInt();
                    System.out.println(resultat1);
                    System.out.println(resultat);

                    String paragraphe = resultat;
                    Label label = new Label(resultat);
                    Button btn = new Button("Exporter !");

                    btn.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            String para2 = paragraphe;
                            // path to create the file
                            String file_path = "/c:/Projet1/AnnuaireRecherche.pdf";

                            // creating PdfWriter object
                            PdfWriter pdf_writer = null;
                            try {
                                pdf_writer = new PdfWriter(file_path);
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }

                            // Representing PdfDocument object
                            PdfDocument pdf_doc = new PdfDocument(pdf_writer);
                            // Creating a Document
                            // Instantiating a document object from pdf document
                            // object
                            Document document = new Document(pdf_doc);

                            // paragraph to be added
                            String para  =


                                    " \r\n                       Le r??sultat de ma recherche \r\n" +
                                            "\r\n" +
                                            "\r\n";

                            // Creating Paragraph object
                            Paragraph paragraph_obj
                                    = new Paragraph(para);
                            paragraph_obj.setBold();
                            paragraph_obj.setUnderline();
                            document.add(paragraph_obj);



                            // Creating Paragraph object
                            Paragraph paragraph_obj2 = new Paragraph(para2);

                            // Adding paragraphs to document
                            document.add(paragraph_obj2);




                            // Closing the document
                            document.close();

                            // final message
                            System.out.println("Finished writing contents to the file!");




                            document.close();

                            getHostServices().showDocument("C:\\Projet1\\AnnuaireRecherche.pdf");


                        }
                    });

                    HBox hbButtons = new HBox();
                    hbButtons.getChildren().add(btn);
                    hbButtons.setAlignment(Pos.BOTTOM_RIGHT);

                    StackPane secondaryLayout = new StackPane();
                    secondaryLayout.getChildren().addAll(label,hbButtons);

                    Scene secondScene = new Scene(secondaryLayout,600,300);
                    String css = this.getClass().getResource("stylesheetrecherche.css").toExternalForm();
                    secondScene.getStylesheets().add(css);

                    //secondScene.getStylesheets().add(getClass().getResource("stylesheetrecherche.css").toExternalForm());
                    Stage newWindow = new Stage();
                    newWindow.setTitle("Annuaire des Stagiaires");
                    newWindow.setScene(secondScene);

                    // Set position of second window, related to primary window.
                    //newWindow.setX(primaryStage.getX() + 200);
                    //newWindow.setY(primaryStage.getY() + 100);

                    newWindow.show();



                }else {

                    if (nomStagRecherche.compareToIgnoreCase(nomStagActuel) < 0) {
                        rechercheStagBoutonRecherche(borneInf, pivot - 1, raf, nomStagRecherche);
                    } else {
                        rechercheStagBoutonRecherche(pivot + 1, borneSup, raf, nomStagRecherche);
                    }
                }

            } else {

                System.out.println("\r\n***** Stagiaire introuvable *****");
            }





        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultat;


    }







    //***************************Fin partie m??thodes***********************************************


    //************************************Cette partie est commune avec ce qu'il ya avant
    TextField prenomTextField = new TextField();
    // prenomTextField.setPromptText("Pr??nom");

    @Override
    public void start(Stage primaryStage) throws Exception {
        //******On va cr??er le RAF on ouvrant l'appl


            createRAF(raf);


        TableView<Stagiaires> table;

        //Cr??ation de la table
        table = new TableView<>();
        table.setPrefSize(700, 490);
        table.setEditable(true);


        //Cr??ation de 5 colonnes
        TableColumn<Stagiaires, String> nomCol =
                new TableColumn<Stagiaires, String>("Nom");
        nomCol.setMinWidth(100);

        //Sp??cifier comment remplir la donn??e pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        nomCol.setCellValueFactory(
                new PropertyValueFactory<Stagiaires, String>("nom"));

        TableColumn<Stagiaires, String> prenomCol = new TableColumn<Stagiaires, String>("Pr??nom");
        nomCol.setMinWidth(100);
        //sp??cifier une pr??f??rence de tri pour cette colonne
        //nomCol.setSortType(TableColumn.SortType.ASCENDING);
        //nomCol.setSortType(TableColumn.SortType.DESCENDING);

        //Sp??cifier comment remplir la donn??e pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        prenomCol.setCellValueFactory(
                new PropertyValueFactory<Stagiaires, String>("prenom"));

        TableColumn<Stagiaires, String> departementCol = new TableColumn<Stagiaires, String>("D??partement");
        departementCol.setMinWidth(100);
        //Sp??cifier comment remplir la donn??e pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        departementCol.setCellValueFactory(
                new PropertyValueFactory<Stagiaires, String>("departement"));


        TableColumn<Stagiaires, String> promoCol = new TableColumn<Stagiaires, String>("Promotion");
        promoCol.setMinWidth(100);
        //sp??cifier une pr??f??rence de tri pour cette colonne
        //nomCol.setSortType(TableColumn.SortType.ASCENDING);
        //nomCol.setSortType(TableColumn.SortType.DESCENDING);

        //Sp??cifier comment remplir la donn??e pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        promoCol.setCellValueFactory(
                new PropertyValueFactory<Stagiaires, String>("promo"));

        TableColumn<Stagiaires, Integer> ann??eCol = new TableColumn<>("Ann??e");
        ann??eCol.setMinWidth(100);
        //Sp??cifier comment remplir la donn??e pour chaque cellule de cette colonne
        //Ceci se fait en specifiant un "cell value factory" pour cette colonne.
        ann??eCol.setCellValueFactory(
                new PropertyValueFactory<>("Ann??e"));


        //Creation des zones de textes et boutons


        TextField nomTextField = new TextField();
        nomTextField.setPromptText("Nom");

        TextField departementTextField = new TextField();
        departementTextField.setPromptText("D??partement");

        TextField promoTextField = new TextField();
        promoTextField.setPromptText("Promotion");

        TextField ann??eTextField = new TextField();
        ann??eTextField.setPromptText("Ann??e");

        // TextField resultatRechercheTextField = new TextField();
        // ann??eTextField.setPromptText("Ann??e ");

        Button actualiserButton = new Button("Actualiser");
        Button modifierButton = new Button("Modifier");
        Button ajouterButton = new Button("Ajouter");
        Button rechercherButton = new Button("Rechercher");
        Button supprimerButton = new Button("Supprimer");
        Button exporterButton = new Button("Exporter");
        // TextField rechercherTxtField = new TextField();
        //  rechercherTxtField.setPromptText("Rechercher");


        nomTextField.setMaxWidth(170);
        prenomTextField.setMaxWidth(170);
        departementTextField.setMaxWidth(170);
        promoTextField.setMaxWidth(170);
        ann??eTextField.setMaxWidth(170);




        // Ajout des Events sur les buttons


        // In case of empty fields , gives alert for respective empty field and requests focus on that field


        // Text fields : Nom , pr??nom , departement , promo , ann??e
        //  Boutons : trier  / ajouter/ rechercher / exporter


        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(nomTextField, prenomTextField, departementTextField, promoTextField, ann??eTextField);

        // Ajouter les items ci dessus dans une Hbox

        VBox vBox1 = new VBox();
        vBox1.setPadding(new Insets(10, 10, 10, 10));
        vBox1.setSpacing(10);
        vBox1.getChildren().addAll(actualiserButton,modifierButton,ajouterButton,rechercherButton,supprimerButton,exporterButton);

        //On ajoute les cinq colonnes ?? la table
        table.getColumns().addAll(nomCol, prenomCol, departementCol, promoCol, ann??eCol);

        //Instanciation de la classe LesStagiaires

        LesStagiaires lesStagiaires = new LesStagiaires("C:\\Projet1\\listeStagiaire.txt");

        // Creer une liste observable des Stagiaires
        RandomAccessFile raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");
        ObservableList<Stagiaires> list = FXCollections.observableArrayList(lectureStag(65, raf));


        // Ajout de la liste au tableau "table"

        table.setItems(list);

        // on cr??e une deuxi??me Hbox et un label pour placer le table et un label pour le rendre plus esth??tique
        // Label lbl2 = new Label("R??sultat de la recherche :");


        // MenuBar
        MenuBar menuBar = new MenuBar();

        // menus
        Menu connexionMenu = new Menu("Connexion");
        Menu administrateursMenu = new Menu("Adminstrateurs");
        Menu aideMenu = new Menu("Aide");

        // MenuItems pour les menus ci dessus

        MenuItem superAdminMenuItem = new MenuItem("super-administrateur");
        MenuItem administrateurMenuItem = new MenuItem("administrateur");
        MenuItem creerAdminsMenuItem = new MenuItem("cr??er des identifiants administrateur");
        MenuItem suppAdminsMenuItem = new MenuItem(" supprimer des identifiants administrateur");
        MenuItem documentationUtilisateurMenuItem = new MenuItem("documentation utilisateur");
        MenuItem d??connexionMenuItem = new MenuItem("D??connexion");
        MenuItem quitterItem = new MenuItem("Quitter");



        // Sp??cifier un raccourci clavier au menuItem Quitter.

        quitterItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));


        //SeparatorMenuItem.

        SeparatorMenuItem separator = new SeparatorMenuItem();

        // Ajouter les menuItems aux Menus
        connexionMenu.getItems().addAll(d??connexionMenuItem, quitterItem);
        administrateursMenu.getItems().addAll(creerAdminsMenuItem, suppAdminsMenuItem);
        aideMenu.getItems().addAll(documentationUtilisateurMenuItem);


        // Ajouter les Menus au MenuBar
        menuBar.getMenus().addAll(connexionMenu, administrateursMenu, aideMenu);

        Button menuPrincipaleButton = new Button();


        VBox vbox2 = new VBox(table);

        //***************************Cette partie es d??di??e pour les actions sur les boutons ***

        suppAdminsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage  = new Stage();

                SupAdmin sup = new SupAdmin ();
                try {
                    sup.start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });


        creerAdminsMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage  = new Stage();

                AjoutAdmin ajt = new AjoutAdmin();
                try {
                    ajt.start(stage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        // Bouton quitter l'application

        quitterItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Platform.exit();
            }
        });
        //Bouton Retour en arri??re
        d??connexionMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage  = new Stage();

                Scene_Acceuil scene_acceuil = new Scene_Acceuil();
                try {
                    primaryStage.close();
                    scene_acceuil.start(stage);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        //Bouton rechercher

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
        // Bouton qui permet de chercher un stagiaire

        rechercherButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                String nomAchercher = nomTextField.getText();
                System.out.println("le nom" + nomAchercher);

                nomAchercher = completer(nomAchercher, NOMSTAGIARE);
                System.out.println("les infos li??es"+ nomAchercher);

                String chaineLabel = rechercheStagBoutonRecherche(0, compteurStag,raf,nomAchercher);
                System.out.println("le stagiaire est  " + chaineLabel);
                nomTextField.clear();



            }


        });


        //Bouton ajouter stagiaires

        ajouterButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nom =nomTextField.getText();
                String prenom=prenomTextField.getText();
                String departement =departementTextField.getText();
                String promo = promoTextField.getText();
                int annee = Integer.parseInt(ann??eTextField.getText());

                Stagiaires contact = new Stagiaires(prenomTextField.getText(),nomTextField.getText(),
                        departementTextField.getText(),
                        promoTextField.getText()
                        ,ann??eTextField.getText());

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
                ann??eTextField.clear();
            }
        });



        supprimerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //list.remove(table.getSelectionModel().getSelectedItem());

                String nom =nomTextField.getText();


                RandomAccessFile newRaf;
                String name = "";


                try {
                    int taille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO;
                    newRaf = new RandomAccessFile("c:/Projet1/listeStagiaire2.bin", "rw");

                    System.out.println("Le nom de stagiaire ?? supprimer " + nom);

                    nom =completer(nom,NOMSTAGIARE);//12/06
                    rechercheStag2(0, compteurStag, raf, nom);

                    raf.seek(raf.getFilePointer()- LONGUEUSTAGIARE);
                    name = lectureStag2(taille,raf) + raf.readInt();
                    System.out.println("the name "+name);

                    raf.seek(0);
                    for (int i = 0 ;i < compteurStag; i++){

                        String chaine = "";
                        String chaine1 = lectureStag2(taille, raf);
                        int nbannee= raf.readInt();
                        chaine = chaine + chaine1 + nbannee;
                        if ( !chaine.equals(name)){

                            newRaf.writeChars(chaine1);
                            newRaf.writeInt(nbannee);
                        }
                    }
                    //compteurStag =-1;
                    System.out.println("la liste dans le nouveau raf\r\n");
                    listeStag(compteurStag-1, newRaf);
                    //**********Avant cette partie tout marche *********
                    // il faut penser comment faire pour que le compteur soit mis ?? jour ( ?? faire le jeudi 09/06)
                    raf.setLength(0);

                    newRaf.seek(0);
                    for (int j = 0; j < compteurStag-1; j++){

                        String infostag = lectureStag2(taille,newRaf);
                        int nb = newRaf.readInt();

                        raf.writeChars(infostag);
                        raf.writeInt(nb);

                    }

                    System.out.println("liste des stagiaires dans le raf");
                    listeStag(compteurStag-1, raf);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                nomTextField.clear();
                prenomTextField.clear();
                departementTextField.clear();
                promoTextField.clear();
                ann??eTextField.clear();
            }});
        actualiserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Il faut mettre ?? jour la liste sur laquelle la table pointe
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



            }
        });
        modifierButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                //*****************La suppression******************

                String nom =nomTextField.getText();


                RandomAccessFile newRaf;
                String name = "";


                try {
                    int taille = NOMSTAGIARE + PRENOMSTAGIARE + DEPARTEMENT + PROMO;
                    newRaf = new RandomAccessFile("c:/Projet1/listeStagiaire2.bin", "rw");

                    System.out.println("Le nom de stagiaire ?? supprimer " + nom);

                    rechercheStag2(0, compteurStag, raf, nom);

                    raf.seek(raf.getFilePointer());
                    name = lectureStag2(taille,raf) + raf.readInt();
                    System.out.println("the name "+name);
                    //*****New*****



                    //******Fin New**************

                    raf.seek(0);
                    for (int i = 0 ;i < compteurStag; i++){

                        String chaine = "";
                        String chaine1 = lectureStag2(taille, raf);
                        int nbannee= raf.readInt();
                        chaine = chaine + chaine1 + nbannee;
                        if ( !chaine.equals(name)){

                            newRaf.writeChars(chaine1);
                            newRaf.writeInt(nbannee);
                        }
                    }
                    //compteurStag =-1;
                    System.out.println("la liste dans le nouveau raf\r\n");
                    listeStag(compteurStag-1, newRaf);
                    //**********Avant cette partie tout marche *********
                    // il faut penser comment faire pour que le compteur soit mis ?? jour ( ?? faire le jeudi 09/06)
                    raf.setLength(0);

                    newRaf.seek(0);
                    for (int j = 0; j < compteurStag-1; j++){

                        String infostag = lectureStag2(taille,newRaf);
                        int nb = newRaf.readInt();

                        raf.writeChars(infostag);
                        raf.writeInt(nb);

                    }

                    System.out.println("liste des stagiaires dans le raf");
                    listeStag(compteurStag-1, raf);

                    prenomTextField.clear();
                    nomTextField.clear();
                    departementTextField.clear();
                    promoTextField.clear();
                    ann??eTextField.clear();

                    //**************Fin suppression ********************

                    RandomAccessFile raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");

                    list.setAll(lectureStag(65,raf));
                    table.setItems(list);


                    //***************Debut Ajout Version2

                    Label label = new Label(name);
                    Button btnRefresh = new Button("Refresh !");
                    StackPane secondaryLayout = new StackPane();
                    HBox hbButtons = new HBox();
                    hbButtons.getChildren().add(btnRefresh);
                    hbButtons.setAlignment(Pos.BOTTOM_RIGHT);

                    btnRefresh.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {

                            String nom =nomTextField.getText();
                            String prenom=prenomTextField.getText();
                            String departement =departementTextField.getText();
                            String promo = promoTextField.getText();
                            int annee = Integer.parseInt(ann??eTextField.getText());

                            Stagiaires contact = new Stagiaires(prenomTextField.getText(),nomTextField.getText(),
                                    departementTextField.getText(),
                                    promoTextField.getText()
                                    ,ann??eTextField.getText());


                            RandomAccessFile rafAjout = null;
                            try {
                                rafAjout = new RandomAccessFile("c:/Projet1/listeStagiaireAjout.bin", "rw");

                                rafAjout.setLength(0);

                                raf.seek(0);

                                for (int i = 0; i < compteurStag-1; i++){
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

                                //compteurStag = compteurStag+1;

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

                                prenomTextField.clear();
                                nomTextField.clear();
                                departementTextField.clear();
                                promoTextField.clear();
                                ann??eTextField.clear();
                                RandomAccessFile raf = new RandomAccessFile("c:/Projet1/listeStagiaire.bin", "rw");

                                list.setAll(lectureStag(65,raf));
                                table.setItems(list);

                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }


                        }
                    });

                    secondaryLayout.getChildren().addAll(label,hbButtons);

                    Scene secondScene = new Scene(secondaryLayout,600,300);
                    String css = this.getClass().getResource("stylesheetrecherche.css").toExternalForm();
                    secondScene.getStylesheets().add(css);

                    //secondScene.getStylesheets().add(getClass().getResource("stylesheetrecherche.css").toExternalForm());
                    Stage newWindow = new Stage();
                    newWindow.setTitle("Annuaire des Stagiaires");
                    newWindow.setScene(secondScene);

                    // Set position of second window, related to primary window.
                    //newWindow.setX(primaryStage.getX() + 200);
                    //newWindow.setY(primaryStage.getY() + 100);

                    newWindow.show();


                    //**************D??but AJout *****************



                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                prenomTextField.clear();
                nomTextField.clear();
                departementTextField.clear();
                promoTextField.clear();
                ann??eTextField.clear();



            }
        });






        //**********************Fin partie action sur les boutons *****************************


        BorderPane root = new BorderPane(null, menuBar, vBox1, hBox, vbox2);

        root.setTop(menuBar);
        root.setLeft(vbox2);
        root.setBottom(hBox);
        root.setAlignment(hBox, Pos.BOTTOM_CENTER);
        root.setRight(vBox1);
        vBox1.setPadding(new Insets(15));
        vBox1.setSpacing(10);
        hBox.setPadding(new Insets(40));
        root.setMargin(vbox2, new Insets(18));


        // Creation de la sc??ne

        Scene scene = new Scene(root);
        scene.getStylesheets().

                add(getClass().

                        getResource("stylesheettbdsuperadmin.css").

                        toExternalForm());
        primaryStage.setTitle("Annuaire des Stagiaires");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(700);
        primaryStage.setScene(scene);
        primaryStage.show();


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
                    System.out.println("Utilisateur trouv??");
                    System.out.println(nomStagActuel.get(0).getNom());
                    System.out.println(nomStagActuel.get(0).getPrenom());
                    System.out.println(nomStagActuel.get(0).getDepartement());
                    System.out.println(nomStagActuel.get(0).getAnn??e());
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
}


