package Hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML
    private ImageView imgBG1, imgBG2, imgBG3;
    @FXML
    private ImageView imgMan, imgWoman;
    @FXML
    private Button bApply, bGoTitle;
    @FXML
    private RadioButton rbEasy, rbMedium, rbHard;

    private int bgNum;
    private boolean isMan;
    private int difficulty;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bgNum = GameController.bgNum;
        isMan = GameController.isMan;
        difficulty = GameController.Difficulty;

        change_BG_Shadow(bgNum);
        change_HUMAN_Shadow(isMan);
        change_Game_Difficulty(difficulty);

        imgBG1.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> change_BG_Shadow(1));
        imgBG2.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> change_BG_Shadow(2));
        imgBG3.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> change_BG_Shadow(3));

        imgMan.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> change_HUMAN_Shadow(true));
        imgWoman.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> change_HUMAN_Shadow(false));

        rbEasy.setOnAction(event -> change_Game_Difficulty(1));
        rbMedium.setOnAction(event -> change_Game_Difficulty(2));
        rbHard.setOnAction(event -> change_Game_Difficulty(3));
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

    private void change_Game_Difficulty(int value) {
        rbEasy.setSelected(false);
        rbMedium.setSelected(false);
        rbHard.setSelected(false);

        if (value == 1) {
            rbEasy.setSelected(true);
        } else if (value == 2) {
            rbMedium.setSelected(true);
        } else if (value == 3) {
            rbHard.setSelected(true);
        }

        difficulty = value;
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
        GameController.Difficulty = this.difficulty;
        try {
            String path = "./data/" + TitleController.LoginedID + ".db";
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String temp_pw = reader.readLine();
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(temp_pw);
            writer.newLine();
            writer.write("Background=" + this.bgNum + "&HumanGender=" + (this.isMan ? "man" : "woman") + "&GameDifficulty=" + this.difficulty);
            writer.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("파일 입출력 오류 발생.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}