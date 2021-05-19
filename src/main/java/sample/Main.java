package main.java.sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage mainStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("pocetniEkran.fxml"));
        primaryStage.setTitle("Covid Portal");
        primaryStage.setScene(new Scene(root, 700, 310));
        primaryStage.show();
        mainStage = primaryStage;
    }

    public static Stage getMainStage(){
        return mainStage;
    }


    public static void main(String[] args) {
        launch(args);

    }
}
