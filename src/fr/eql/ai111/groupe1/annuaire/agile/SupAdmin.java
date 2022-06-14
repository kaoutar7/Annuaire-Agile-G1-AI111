package fr.eql.ai111.groupe1.annuaire.agile;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SupAdmin extends Application {
    ReaderDAO dao = new ReaderDAO();
    ArbreDansLeRAF arbreDansLeRAF = new ArbreDansLeRAF();

    @Override
    public void start(Stage primaryStage) throws Exception {
        //créer une grille (GridPane Layout)
        GridPane grille = new GridPane();
        grille.setAlignment(Pos.CENTER);
        grille.setHgap(10);
        grille.setVgap(10);
        grille.setPadding(new Insets(20, 20, 20, 20));

        //Remplir la grille
        Text titre = new Text("Fenêtre d'Authentification");
        //titre.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        //Utilisé dans le fichier css pour identifier le noeud
        //titre.setId("titreText");

//        File root = new File("C:/Users/Formation/Documents/Projects/Annuaire-Agile-G1-AI111/Connexion");
//        FileReader fr = new FileReader("Connexion");


//        ObservableList <String> data =createFileList() ;
        File root = new File("Connexion");
        String[] content = root.list();


        ChoiceBox roleBox = new ChoiceBox();
        roleBox.getItems().addAll
                (content);
//        Label loginLabel = new Label("Login");
//        TextField loginNew = new TextField();
//        loginNew.setPromptText("login");
//        loginNew.setFocusTraversable(false);
        grille.add(roleBox,0,2);
//        grille.add(loginLabel,0,2);
//        grille.add(loginNew,0,3);

        Button btnSup = new Button("Supprimer");
        btnSup.getStyleClass().add("fancy-button");
        HBox hbBtnSup = new HBox(10);
        hbBtnSup.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnSup.getChildren().add(btnSup);
        grille.add(hbBtnSup, 0, 4);


                   // TextField passwordNew = new TextField("Password");
                   // grille.add(passwordNew,0,4);
        btnSup.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {


                File renamedFile = new File("Connexion/" +roleBox.getValue());
//                File renamedFile = new File("Connexion/" +loginNew.getText() + ".lib");
                boolean isRenamed = renamedFile.renameTo(renamedFile);
                boolean isDeleted = renamedFile.delete();

//
//                Scene_tableauDeBordSuperAdmin tdb  = new Scene_tableauDeBordSuperAdmin();
//                try {
//                    tdb.start(primaryStage);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
                Alert confirmerSortie = new Alert(Alert.AlertType.CONFIRMATION);
                confirmerSortie.setTitle("Confirmation");
                confirmerSortie.setHeaderText(null);
                confirmerSortie.setContentText("Le compte Administrateur  " + roleBox.getValue() + "  a été supprimé avec succès. " );
//                confirmerSortie.setContentText("Le compte Administrateur  " + roleBox.getValue() + "  a été supprimé avec succès. " );
                confirmerSortie.setGraphic(null);
                confirmerSortie.getButtonTypes().removeAll(ButtonType.OK,ButtonType.CANCEL);
                ButtonType btnOui = new ButtonType("ok");
//                ButtonType btnNon = new ButtonType("Non");
                confirmerSortie.getButtonTypes().addAll(btnOui/*btnNon*/);
                Optional<ButtonType> resultat = confirmerSortie.showAndWait();
//                if (resultat.get() == btnOui)
//                    System.exit(0);
            }
        });


        Button btnRetour = new Button("Retour");
        HBox hbBtnRetour = new HBox(10);
        btnRetour.getStyleClass().add("fancy-button");
        hbBtnRetour.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnRetour.getChildren().add(btnRetour);
        grille.add(hbBtnRetour, 0, 5);
        btnRetour.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Scene_tableauDeBordSuperAdmin tdbSuperAdmin = new Scene_tableauDeBordSuperAdmin();
                try {
                    tdbSuperAdmin.start(primaryStage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        });


        Scene scene = new Scene(grille, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("supAdmin.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Fenêtre d'Authentification");
        primaryStage.show();
    }

//    public Set<String> listFilesUsingJavaIO(String dir) {
//        return Stream.of(new File(dir).listFiles())
//                .filter(file -> !file.isDirectory())
//                .map(File::getName)
//                .collect(Collectors.toSet());
//    }
//
//    private ObservableList<String> createFileList() {
//
//        SupAdmin accounts = null;
//        ObservableList<String> list = FXCollections.observableArrayList(accounts.listFilesUsingJavaIO("Connexion"));
//        return list;
//    }
}
