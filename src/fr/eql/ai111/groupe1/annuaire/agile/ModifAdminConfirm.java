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
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.util.Optional;

public class ModifAdminConfirm extends Application {
    ReaderDAO dao = new ReaderDAO();

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

        TextField loginNew = new TextField();
        loginNew.setPromptText(" Nouveau login");
        loginNew.setFocusTraversable(false);
        grille.add(loginNew,0,3);

        TextField passwordNew = new TextField();
        passwordNew.setPromptText("Nouveau password");
        passwordNew.setFocusTraversable(false);
        grille.add(passwordNew,0,4);

        Label roleNew = new Label("admin");
       // grille.add(roleNew,0,5);

        Button btnAjout = new Button(" Enregistrer ");
        btnAjout.getStyleClass().add("fancy-button");
        HBox hbBtnAjout = new HBox(10);
        hbBtnAjout.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnAjout.getChildren().add(btnAjout);
        grille.add(hbBtnAjout, 0, 6);

        btnAjout.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {





                boolean isCreated = dao.createAccount(loginNew.getText(), passwordNew.getText(),roleNew.getText());

                if (!isCreated) {
                    Label alert = new Label("Ce login n'est pas disponible. Veuillez en choisir un autre.");
                }
                Alert confirmerSortie = new Alert(Alert.AlertType.CONFIRMATION);
                confirmerSortie.setTitle("Confirmation");
                confirmerSortie.setHeaderText(null);
                confirmerSortie.setGraphic(null);
                confirmerSortie.setContentText("Vos modifications ont été enregistrées avec succès" );
                confirmerSortie.getButtonTypes().removeAll(ButtonType.OK,ButtonType.CANCEL);
                ButtonType btnOui = new ButtonType("ok");
//                ButtonType btnNon = new ButtonType("Non");
                confirmerSortie.getButtonTypes().addAll(btnOui/*btnNon*/);
                Optional<ButtonType> resultat = confirmerSortie.showAndWait();
//                if (resultat.get() == btnOui)
//                    System.exit(0);

            }

        });
        Button btnRetour1 = new Button("Retour");
        HBox hbBtnRetour1 = new HBox(10);
        hbBtnRetour1.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnRetour1.getChildren().add(btnRetour1);
        grille.add(hbBtnRetour1, 0, 7);
        btnRetour1. getStyleClass().add("fancy-button");
        btnRetour1.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Scene_tableauDeBordAdmin st = new Scene_tableauDeBordAdmin();
                try {
                    st.start(primaryStage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        });

        Scene scene = new Scene(grille, 1000, 700);
        //scene.getStylesheets().add(getClass().getResource("loginForm.css").toExternalForm());

        primaryStage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("modifierAdmin.css").toExternalForm());
        primaryStage.setTitle("Modifier mon profil ");
        primaryStage.show();
    }
}
