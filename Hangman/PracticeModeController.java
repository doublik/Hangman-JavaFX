package Hangman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PracticeModeController implements Initializable {

    private Timeline timeline;
    private final int tick = 3;
    private int lefttick;

    @FXML private BorderPane background;
    @FXML private Label txtCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        startCountDown();
    }

    private void startGame() {
        background.setStyle("-fx-background-color: WHITE");
    }

    private void startCountDown() {
        lefttick = tick;
        txtCount.setText("" + lefttick);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1),event -> {
            lefttick--;
            if (lefttick <= 0) {
                timeline.stop();
                txtCount.setVisible(false);
                startGame();
            } else { // if lefttick is 0, Text don't change.
                txtCount.setText("" + lefttick);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}