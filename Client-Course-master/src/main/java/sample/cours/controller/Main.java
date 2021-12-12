package sample.cours.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.cours.service.Client;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("Bank for you");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {

        Client client = new Client();
        client.connectToServer();
        launch(args);
    }

}
