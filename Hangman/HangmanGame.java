package Hangman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class HangmanGame extends Application {

    @Override
    public void start(Stage window) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/TitleScreen.fxml"));

        window.setTitle("HANGMAN");
        window.setResizable(false);
        window.centerOnScreen();
        window.setScene(new Scene(root, 326, 200));
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
