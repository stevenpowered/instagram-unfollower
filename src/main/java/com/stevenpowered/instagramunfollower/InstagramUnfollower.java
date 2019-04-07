package com.stevenpowered.instagramunfollower;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoginResult;

import java.io.IOException;

public class InstagramUnfollower extends Application {

    private static InstagramUnfollower instance;
    private Instagram4j instagram;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        instance = this;

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent parent = fxmlLoader.load(getClass().getResourceAsStream("/fxml/login.fxml"));

        Scene scene = new Scene(parent);
        scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static InstagramUnfollower getInstance() {
        return instance;
    }

    public InstagramLoginResult setup(String username, String password) throws IOException {
        instagram = Instagram4j.builder().username(username).password(password).build();
        instagram.setup();
        return instagram.login();
    }

    public Instagram4j getInstagram() {
        return instagram;
    }
}
