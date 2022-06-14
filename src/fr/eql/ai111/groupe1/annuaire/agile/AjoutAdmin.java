package fr.eql.ai111.groupe1.annuaire.agile;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.swing.JOptionPane;
import java.util.Optional;

public class AjoutAdmin extends Application {
    ReaderDAO dao = new ReaderDAO();
    ArbreDansLeRAF arbreDansLeRAF = new ArbreDansLeRAF();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

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

        Label loginLabel = new Label("Login");
        TextField loginNew = new TextField();
        //loginNew.setPromptText("login");
       // loginNew.setFocusTraversable(false);
       grille.add(loginLabel,0,13);
        grille.add(loginNew,0,14);

        Label passwordLabel = new Label("Password");
        TextField passwordNew = new TextField();
       // passwordNew.setPromptText("password");
        //passwordNew.setFocusTraversable(false);
        grille.add(passwordLabel,0,15);
        grille.add(passwordNew,0,16);


        Label roleLabel = new Label("Role");

        //Choice box for location
        ChoiceBox roleBox = new ChoiceBox();
        roleBox.getItems().addAll
                ("admin", "super-administrateur");

        roleBox.getStyleClass().add("fancy-button");
        grille.add(roleLabel,0,17);
        grille.add(roleBox,0,18);

        Button btnAjout = new Button("Ajouter");
        btnAjout.getStyleClass().add("fancy-button");
        HBox hbBtnAjout = new HBox(22);
        hbBtnAjout.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnAjout.getChildren().add(btnAjout);
        grille.add(hbBtnAjout, 0, 23);
        Button btnRetour = new Button("Retour");
        btnRetour.getStyleClass().add("fancy-button");
        HBox hbBtnRetour = new HBox(10);
        hbBtnRetour.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnRetour.getChildren().add(btnRetour);
        grille.add(hbBtnRetour, 0, 24);

        btnAjout.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {


                System.out.println(roleBox.getValue());



                boolean isCreated = dao.createAccount(loginNew.getText(), passwordNew.getText(), (String) roleBox.getValue());
                if (!isCreated) {
                    Scene_tableauDeBordSuperAdmin tdbSuperAdmin = new Scene_tableauDeBordSuperAdmin();
                    try {
                        tdbSuperAdmin.start(primaryStage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    JOptionPane.showMessageDialog(null,
                            "Ce login n'est pas disponible. " +
                                    "Veuillez en choisir un autre.");
                }
                System.out.println("C'est un ajout du SA");

                Alert confirmerSortie = new Alert(Alert.AlertType.CONFIRMATION);
                confirmerSortie.setTitle("Confirmation");
                confirmerSortie.setGraphic(null);
                confirmerSortie.setHeaderText(null);
                confirmerSortie.setContentText("L'administrateur " + loginNew.getText() + " a été créé avec succès. ");
                confirmerSortie.getButtonTypes().removeAll(ButtonType.OK,ButtonType.CANCEL);
                ButtonType btnOui = new ButtonType("ok");
//                ButtonType btnNon = new ButtonType("Non");
                confirmerSortie.getButtonTypes().addAll(btnOui/*btnNon*/);
                Optional<ButtonType> resultat = confirmerSortie.showAndWait();
//                if (resultat.get() == btnOui)
//                    System.exit(0);

            }

        });




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

        Scene scene2 = new Scene(grille, 1000, 700);

        scene2.getStylesheets().add(getClass().getResource("AjoutAdmin.css").toExternalForm());

        primaryStage.setScene(scene2);
        primaryStage.setTitle("Fenêtre d'ajout");
        primaryStage.show();
    }
}
