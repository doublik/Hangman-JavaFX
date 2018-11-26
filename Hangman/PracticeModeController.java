package Hangman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class PracticeModeController implements Initializable {

    @FXML private AnchorPane GamePane;
    @FXML private BorderPane CountDownPane;
    @FXML private Label txtCount, txtDelete;
    @FXML private ProgressBar timeBar;
    @FXML private ImageView human;
    @FXML private Label rope;

    private Timeline timeline;
    private final int tick = 3;
    private final int GameTime = 60000; // ms (60000 = 1 min)
    private int leftTick, leftTime;
    private final String[] words = new String[] {"APPLE","STUDENT","CHARACTER"};
    private char[] answer;
    private char[] uranswer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CountDownPane.setVisible(true);
        timeBar.setStyle("-fx-accent: #ffae00");
        startCountDown();
        // set word
        Random random = new Random();
        String temp = words[random.nextInt(words.length)]; //words[(int)Math.round(Math.random()*(words.length-1))];
        answer = new char[temp.length()];
        uranswer = new char[temp.length()];
        for (int i=0; i<temp.length(); i++) {
            answer[i] = temp.charAt(i);
            uranswer[i] = '.';
            System.out.println(answer[i] + "" + uranswer[i]);
        }
    }

    private void startGame() {
        CountDownPane.setVisible(false);

        leftTime = GameTime;
        timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            leftTime -= 10;
            if (leftTime <= 0) {
                timeline.stop();
            }
            timeBar.setProgress((double)leftTime/60000);
            downRope(0.081);
            if ((human.getLayoutY()+332 >= 570) && (leftTime%100 == 0)) { //((leftTime < 40000) && (leftTime%100 == 0)) {
                if (txtDelete.getTextFill() == Paint.valueOf("ORANGE")) {
                    txtDelete.setTextFill(Paint.valueOf("WHITE"));
                } else {
                    txtDelete.setTextFill(Paint.valueOf("ORANGE"));
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void downRope(double value) {
        rope.setPrefHeight(rope.getPrefHeight() + value);
        human.setLayoutY(human.getLayoutY() + value);
    }

    private void startCountDown() {
        leftTick = tick;
        txtCount.setText("" + leftTick);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1),event -> {
            leftTick--;
            if (leftTick <= 0) {
                timeline.stop();
                startGame();
            } else { // if lefttick is 0, Text don't change.
                txtCount.setText("" + leftTick);
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}