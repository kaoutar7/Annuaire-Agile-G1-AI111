package fr.eql.ai111.groupe1.annuaire.agile;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.Optional;

public class Scene_login extends Application {

    Scene_tableauDeBordAdmin sceneTbdUtilisateur2 = new Scene_tableauDeBordAdmin();
    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane root = new BorderPane();

        GridPane grid = new GridPane();

        Label idlbl = new Label("Identifiant");
        TextField identifiantTxtField = new TextField();
        identifiantTxtField.setPrefWidth(300);
        Label passwordlbl = new Label("Password");
        PasswordField txtMotdePasse = new PasswordField();
        txtMotdePasse.setPrefWidth(300);
        Button seConnecterBtn = new Button("Se connecter");

        ButtonBar btnB = new ButtonBar();
        ButtonBar btnB1 = new ButtonBar();


        Button quitterBtn = new Button("Quitter");
        Button btnRetour = new Button("Retour");

        btnB.getButtons().addAll(quitterBtn,btnRetour);
        btnB1.getButtons().addAll(seConnecterBtn);


        grid.add(idlbl,0,7,1,1);
        grid.add(identifiantTxtField,0,8,1,1);
        grid.add(passwordlbl,0,9,1,1);
        grid.add(txtMotdePasse,0,10,1,1);
        grid.add(btnB1,0,16,1,1);
        grid.add(btnB,0,18,1,1);



        root.setLeft(grid);
        root.setMargin(grid, new Insets(110));
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(20);
        grid.setVgap(12);
        // grid.setGridLinesVisible(true);
        Scene scene1 = new Scene(root, 1000, 700);

        // Donner à la scène la référence de la stylesheet css
        scene1.getStylesheets().add(getClass().getResource("stylesheetlogin.css").toExternalForm());


        primaryStage.setTitle("Authentification");
        primaryStage.setScene(scene1);
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                fermerProgramme();
            }
        });

        seConnecterBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Administrateur reader;
                ReaderDAO dao = new ReaderDAO();

                reader = dao.connect(identifiantTxtField.getText(),  txtMotdePasse.getText());


                if (reader ==null){

                    //  label.setText("Login ou mot de passe faux"); //Ajout du label
                }
                else if (reader != null)
                    System.out.println("Bienvenue " + identifiantTxtField.getText() + " " + reader.getRole());
                //varLog= identifiantTxtField.getText();
                //System.out.println("Le varLog est :" + varLog);
                if (reader.getRole().equals("admin"))
                {
                    Scene_tableauDeBordAdmin tdbAdmin = new Scene_tableauDeBordAdmin();
                    try {
                        Stage stage  = new Stage();

                        primaryStage.close();
                        tdbAdmin.start(stage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Scene_tableauDeBordSuperAdmin tdbSuperAdmin = new Scene_tableauDeBordSuperAdmin();
                    try {
                        Stage stage  = new Stage();

                        primaryStage.close();
                        tdbSuperAdmin.start(stage);

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });
        quitterBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                event.consume();
                fermerProgramme();
            }
        });

        btnRetour.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene_Acceuil scene_acceuil= new Scene_Acceuil();
                try {
                    scene_acceuil.start(primaryStage);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void fermerProgramme() {
        Alert confirmerSortie = new Alert(Alert.AlertType.CONFIRMATION);
        confirmerSortie.setTitle("Confirmation");
        confirmerSortie.setHeaderText(null);
        confirmerSortie.setContentText("voulez vous vraiment quitter ?");
        confirmerSortie.getButtonTypes().removeAll(ButtonType.OK,ButtonType.CANCEL);
        ButtonType btnOui = new ButtonType("Oui");
        ButtonType btnNon = new ButtonType("Non");
        confirmerSortie.getButtonTypes().addAll(btnOui,btnNon);
        Optional<ButtonType> resultat = confirmerSortie.showAndWait();
        if (resultat.get() == btnOui)
            System.exit(0);


    }

    public static void main(String[] args) {
        launch();
    }

}











