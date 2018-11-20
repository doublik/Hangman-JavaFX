package Hangman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HangmanGame extends Application {

    @Override
    public void start(Stage title) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("./fxml/titleScreen.fxml"));

        title.setTitle("HANGMAN");
        title.setResizable(false);
        title.centerOnScreen();
        title.setScene(new Scene(root,326,200));
        title.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
