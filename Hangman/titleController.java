package Hangman;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class titleController implements Initializable {
    @FXML private Button easyMode;
    @FXML private Button generalMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        easyMode.setOnAction(event -> eMode_Click(event));
        generalMode.setOnAction(event -> gMode_Click(event));
    }

    private void enable_all() {
        easyMode.setDisable(false);
        generalMode.setDisable(false);
    }

    private void disable_all() {
        easyMode.setDisable(true);
        generalMode.setDisable(true);
    }

    // 이지모드 버튼 클릭
    private void eMode_Click(ActionEvent event) {
        System.out.println("EASY MODE");
        disable_all();
    }

    // 일반모드 버튼 클릭
    private void gMode_Click(ActionEvent event) {
        System.out.println("GENERAL MODE");
        disable_all();
    }
}
