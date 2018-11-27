package Hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TitleController implements Initializable {

    @FXML private Button bGameStart, bSetting;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    // 게임시작 버튼 클릭
    @FXML
    private void bGS_Action(ActionEvent event) throws IOException {
        Stage stage = (Stage) bGameStart.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/GameScreen.fxml"));
        Scene scene = new Scene(root,800,600);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    // 세팅 버튼 클릭
    @FXML
    private void bS_Action(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/SettingScreen.fxml"));
    }
}
