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

    @FXML private Button practiceMode, generalMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    private void enable_all() {
        practiceMode.setDisable(false);
        generalMode.setDisable(false);
    }

    private void disable_all() {
        practiceMode.setDisable(true);
        generalMode.setDisable(true);
    }

    // 이지모드 버튼 클릭
    @FXML
    private void pMode_Action(ActionEvent event) throws IOException {
        Stage stage = (Stage) practiceMode.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("fxml/PracticeModeScreen.fxml"));
        Scene scene = new Scene(root,800,600);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    // 일반모드 버튼 클릭
    @FXML
    private void gMode_Action(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/GeneralModeScreen.fxml"));
    }
}
