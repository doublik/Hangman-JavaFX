package Hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML
    private ImageView imgBG1, imgBG2, imgBG3;
    @FXML
    private ImageView imgMan, imgWoman;
    @FXML
    private Button bApply, bGoTitle;

    private int bgNum;
    private boolean isMan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bgNum = GameController.bgNum;
        isMan = GameController.isMan;

        if (bgNum == 1) {
            imgBG1.setEffect(new DropShadow());
        } else if (bgNum == 2) {
            imgBG2.setEffect(new DropShadow());
        } else {
            imgBG3.setEffect(new DropShadow());
        }

        if (isMan) {
            imgMan.setEffect(new DropShadow());
        } else {
            imgWoman.setEffect(new DropShadow());
        }

        imgBG1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> change_BG_Shadow(1));
        imgBG2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> change_BG_Shadow(2));
        imgBG3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> change_BG_Shadow(3));

        imgMan.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> change_HUMAN_Shadow(true));
        imgWoman.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> change_HUMAN_Shadow(false));
    }

    private void change_BG_Shadow(int value) {
        imgBG1.setEffect(null);
        imgBG2.setEffect(null);
        imgBG3.setEffect(null);

        if (value == 1) {
            imgBG1.setEffect(new DropShadow());
        } else if (value == 2) {
            imgBG2.setEffect(new DropShadow());
        } else {
            imgBG3.setEffect(new DropShadow());
        }

        bgNum = value;
    }

    private void change_HUMAN_Shadow(boolean value) {
        imgMan.setEffect(null);
        imgWoman.setEffect(null);

        if (value) { // if man clicked
            imgMan.setEffect(new DropShadow());
        } else {
            imgWoman.setEffect(new DropShadow());
        }

        isMan = value;
    }

    @FXML
    private void GoTitle_Action(ActionEvent event) throws IOException {
        Stage stage = (Stage) bGoTitle.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/TitleScreen.fxml"));
        Scene scene = new Scene(root,326,200);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    private void Apply_Action(ActionEvent event) throws IOException {
        GameController.bgNum = this.bgNum;
        GameController.isMan = this.isMan;
    }
}