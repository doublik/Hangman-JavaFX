package Hangman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML private AnchorPane GamePane;
    @FXML private BorderPane CountDownPane;
    @FXML private Label txtCount, txtDelete;
    @FXML private ProgressBar timeBar;
    @FXML private ImageView human;
    @FXML private Label rope;
    private Label[] labelAns, labelUnderline;
    private Button[] bAlphabet;
    private Label txtEndingMsg;
    private Button bBackToTitle;

    private Timeline timeline;
    private final int tick = 3;
    private final int GameTime = 60000; // ms (60000 = 1 min)
    private int leftTick, leftTime;
    private final String[] words = {"STUDENT","CHARACTER","MANTIS","ANIMAL","SMASHROOM","HANYANG","OHIKJUN","LEEJUNGIN","MAPLELEAF",
    "ILOVEYOU","COMMUNITY","PROBLEM","DISCRETE","SUBMARINE","SEOYOUNG","LEEEUNAH"};
    private char[] answer;
    private char[] uranswer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CountDownPane.setVisible(true);
        timeBar.setStyle("-fx-accent: #ffae00");
        startCountDown();

        // set word
        Random random = new Random();
        //String temp = words[random.nextInt(words.length)];
        String temp = words[(int)Math.round(Math.random()*(words.length-1))];
        answer = new char[temp.length()];
        uranswer = new char[temp.length()];
        labelAns = new Label[temp.length()];
        labelUnderline = new Label[temp.length()];
        for (int i=0; i<temp.length(); i++) {
            answer[i] = temp.charAt(i);
            uranswer[i] = '_';

            labelAns[i] = new Label("labelAns" + i);
            labelAns[i].setPrefSize(52,100);
            labelAns[i].setLayoutX(320+(25*(9-temp.length()))+(52*i));
            labelAns[i].setLayoutY(90);
            labelAns[i].setText(" ");
            //labelAns[i].setUnderline(true);
            labelAns[i].setFont(Font.font("Arial", 70));
            labelAns[i].setAlignment(Pos.CENTER);

            labelUnderline[i] = new Label("labelUnderline" + i);
            labelUnderline[i].setPrefSize(48, 3);
            labelUnderline[i].setLayoutX(320+(25*(9-temp.length()))+(52*i)+2);
            labelUnderline[i].setLayoutY(175);
            labelUnderline[i].setText("");
            labelUnderline[i].setFont(Font.font("Arial",1));
            labelUnderline[i].setStyle("-fx-background-color: BLACK");
            GamePane.getChildren().addAll(labelAns[i], labelUnderline[i]);
        }

        // make alphabet select button
        bAlphabet = new Button[26];
        for (int i=0; i<26; i++) {
            bAlphabet[i] = new Button("bAlphabet" + i);
            bAlphabet[i].setPrefSize(80,37);
            if (i == 25) bAlphabet[i].setPrefSize(408,37);
            bAlphabet[i].setLayoutX(350+(82*(i%5)));
            bAlphabet[i].setLayoutY(250+(i/5)*45);
            bAlphabet[i].setText("" + (char)((int)'A'+i));
            bAlphabet[i].setFont(Font.font("Airal", 20));
            Button bTmp = bAlphabet[i];
            bAlphabet[i].setOnAction(event -> PressButton(bTmp.getText()));
            GamePane.getChildren().add(bAlphabet[i]);
        }

        // end message ( win or lose )
        txtEndingMsg = new Label("txtEndingMsg");
        txtEndingMsg.setPrefSize(490, 110);
        txtEndingMsg.setText("ENDING MSG");
        txtEndingMsg.setFont(Font.font("Arial", 50));
        txtEndingMsg.setAlignment(Pos.CENTER);
        txtEndingMsg.setLayoutX(310);
        txtEndingMsg.setLayoutY(240);
        txtEndingMsg.setVisible(false);

        // go back to title
        bBackToTitle = new Button("bBackToTitle");
        bBackToTitle.setPrefSize(150,37);
        bBackToTitle.setLayoutX(600);
        bBackToTitle.setLayoutY(500);
        bBackToTitle.setText("GO TITLE");
        bBackToTitle.setFont(Font.font("Arial", 20));
        bBackToTitle.setOnAction(event -> backToTitle());
        bBackToTitle.setVisible(false);

        GamePane.getChildren().addAll(txtEndingMsg, bBackToTitle);
    }

    private void PressButton(String getText) {
        int countSpace = 0, countCorrect = 0;
        char key = getText.charAt(0);
        bAlphabet[key-'A'].setVisible(false);
        for (int i=0; i<answer.length; i++) {
            if (answer[i] == key) {
                uranswer[i] = key;
                labelAns[i].setText("" + key);
                countCorrect++;
            }
            if (labelAns[i].getText() == " ") {
                countSpace++;
            }
        }
        if (countSpace == 0) {
            winGame();
            return;
        }
        if (countCorrect == 0) {
            leftTime -= 3000;
            downRope(0.081 * 300);
        }
    }

    private void startGame() {
        CountDownPane.setVisible(false);

        leftTime = GameTime;
        timeline = new Timeline(new KeyFrame(Duration.millis(10), event -> {
            leftTime -= 10;
            if (leftTime <= 0) {
                timeline.stop();
                timeBar.setProgress(0);
                loseGame();
            } else {
                timeBar.setProgress((double) leftTime / 60000);
                downRope(0.081);
                if ((human.getLayoutY() + 332 >= 570) && (leftTime % 100 == 0)) { //((leftTime < 40000) && (leftTime%100 == 0)) {
                    if (txtDelete.getTextFill() == Paint.valueOf("ORANGE")) {
                        txtDelete.setTextFill(Paint.valueOf("WHITE"));
                    } else {
                        txtDelete.setTextFill(Paint.valueOf("ORANGE"));
                    }
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

    private void hideAllButton() {
        for (int i=0; i<26; i++) {
            bAlphabet[i].setVisible(false);
        }
    }

    private void backToTitle() {
        try {
            Stage stage = (Stage) GamePane.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("fxml/TitleScreen.fxml"));
            Scene scene = new Scene(root, 326, 200);
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {

        }
    }

    private void loseGame() {
        hideAllButton();

        txtEndingMsg.setText("YOU LOSE!!");
        txtEndingMsg.setVisible(true);
        bBackToTitle.setVisible(true);
    }

    private void winGame() {
        timeline.stop();
        hideAllButton();

        txtEndingMsg.setText("CORRECT!!");
        txtEndingMsg.setVisible(true);
        bBackToTitle.setVisible(true);

        Label txtRecord = new Label("txtRecord");
        txtRecord.setPrefSize(490, 100);
        txtRecord.setText("SCORE : " + leftTime/60 + " / 1000");
        txtRecord.setTextFill(Paint.valueOf("GREEN"));
        txtRecord.setFont(Font.font("Arial", 40));
        txtRecord.setAlignment(Pos.CENTER);
        txtRecord.setLayoutX(310);
        txtRecord.setLayoutY(300);
        GamePane.getChildren().add(txtRecord);
    }
}