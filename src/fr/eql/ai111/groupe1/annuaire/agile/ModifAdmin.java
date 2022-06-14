package fr.eql.ai111.groupe1.annuaire.agile;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.Reader;

public class ModifAdmin extends Application {
    ReaderDAO dao = new ReaderDAO();
    Administrateur reader;
    Scene_login scene_login;
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
        loginNew.setPromptText(" ancien login");
        loginNew.setFocusTraversable(false);
        grille.add(loginNew,0,3);

        TextField passwordNew = new TextField();
        passwordNew.setPromptText(" ancien password");
        passwordNew.setFocusTraversable(false);
        grille.add(passwordNew,0,4);

       // Label roleNew = new Label("admin");
        //grille.add(roleNew,0,5);

        Button btnAjout = new Button(" Modifier");
        btnAjout.getStyleClass().add("fancy-button");
        HBox hbBtnAjout = new HBox(10);
        hbBtnAjout.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnAjout.getChildren().add(btnAjout);
        grille.add(hbBtnAjout, 0, 5);

        btnAjout.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {



                File renamedFile = new File("Connexion/" +loginNew.getText() + ".lib");
                boolean isRenamed = renamedFile.renameTo(renamedFile);
                boolean isDeleted = renamedFile.delete();

//                boolean isCreated = dao.createAccount(loginNew.getText(), passwordNew.getText(),roleNew.getText());
//
//                if (!isCreated) {
//                    Label alert = new Label("Ce login n'est pas disponible. Veuillez en choisir un autre.");
//                }

                ModifAdminConfirm mad = new ModifAdminConfirm();
                try {
                    mad.start(primaryStage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            }

        });
        Button btnRetour = new Button("Retour");
        btnRetour.getStyleClass().add("fancy-button");
        HBox hbBtnRetour = new HBox(10);
        hbBtnRetour.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtnRetour.getChildren().add(btnRetour);
        grille.add(hbBtnRetour, 0, 6);
        btnRetour.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                Scene_tableauDeBordAdmin admin = new Scene_tableauDeBordAdmin();
                try {
                    admin.start(primaryStage);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        });

        Scene scene = new Scene(grille, 1000, 700);
        scene.getStylesheets().add(getClass().getResource("modifierAdmin.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Modifier mon profil ");
        primaryStage.show();

    }
}
