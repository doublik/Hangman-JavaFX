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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML private AnchorPane GamePane;
    @FXML private BorderPane CountDownPane;
    @FXML private Label txtCount, txtDelete;
    @FXML private ProgressBar timeBar;
    @FXML private ImageView background;
    @FXML private ImageView human;
    @FXML private Label rope;
    private Label[] labelAns, labelUnderline;
    private Button[] bAlphabet;
    private Label txtEndingMsg;
    private Button bBackToTitle;

    // default value
    public static int bgNum = 1;
    public static boolean isMan = true;
    public static int Difficulty = 1;

    private Timeline timeline;
    private final int tick = 3;
    private final int GameTime = 60000; // ms (60000 = 1 min)
    private int leftTick, leftTime;
    private int wrongCount = 0;
    private static String[][] words = {
            {"mirror","evening","market","jungle","digital","insert","delete","program","english","museum",
                    "vacation","basket","middle","guitar","captain","hundred","center","sheep","follow","window",
                    "summer","winter","fresh","peace","repeat","potato","stupid","finger","hiking","island","capital",
                    "holiday","attach","change","flame","survive","natural","labor","address","bottom","terrible",
                    "special","nervous","biology","pretend","pretty"}, // EASY (DIFFICULTY = 1)
            {"database","breakfast","memorize","expertise","available","criticize","submarine",
                    "through","finance","vegetable","security","traveling","software","understand",
                    "apartment","narrow","beautiful","pardon","excellent","yesterday","magazine","happiness",
                    "symbol","persuade","stretch","distance","physical","attitude","average","decorate","harmony",
                    "tendency","memorial","fatigue","reliance","surround"}, // MEDIUM (DIFFICULTY = 2)
            {"trapezoid","Singapore","dictionary","instruction", "immigrant","appetite","technical",
                    "merchant","listening","semantic","guarantee", "mankind","architect","withstand",
                    "antipathy","flourish","digestion", "briefcase","microscope","proofread","carpenter"} // HARD (DIFFICULTY = 3)
    };
    private static int totalWords;
    private int random;
    private char[] answer;
    private char[] uranswer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CountDownPane.setVisible(true);
        timeBar.setStyle("-fx-accent: #ffae00"); // 색상
        startCountDown();

        // load setting
        background.setImage(new Image(getClass().getResourceAsStream("img/bg" + bgNum + ".png")));
        if (isMan) {
            human.setImage(new Image(getClass().getResourceAsStream("img/human.png")));
        } else {
            human.setImage(new Image(getClass().getResourceAsStream("img/human_w.png")));
        }

        // set word
        totalWords = words[Difficulty-1].length;
        random = (int)Math.floor(Math.random()*totalWords);
        String temp = words[Difficulty-1][random].toUpperCase(); // 모두 대문자로
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
            // 틀릴때마다 가중치 붙어서 패널티. (0.5씩 늘어남)
            leftTime -= 3000 + (wrongCount * 500);
            downRope(0.081 * (300 + (wrongCount * 50)));
            wrongCount++;
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

        txtEndingMsg.setText("YOU DEAD!!");
        txtEndingMsg.setVisible(true);
        bBackToTitle.setVisible(true);
    }

    private void winGame() {
        timeline.stop();
        hideAllButton();

        // 중복 방지
        for (int i=random; i<totalWords-1; i++) {
            words[Difficulty-1][i] = words[Difficulty-1][i+1];
        }
        if (totalWords > 1) {
            totalWords--;
        } else { // ENDING
            words[Difficulty-1][0] = "END";
        }

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