package com.stevenpowered.instagramunfollower;

import com.jfoenix.controls.*;
import com.stevenpowered.instagramunfollower.services.LoginService;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    public JFXTextField inputUsername;
    public JFXTextField inputPassword;
    public JFXButton btnLogin;
    public JFXDialog dialog;
    public StackPane root;
    public Hyperlink linkAuthor;
    public Hyperlink linkSource;

    public void initialize(URL location, ResourceBundle resources) {
        btnLogin.setOnMouseClicked(event -> {
            root.getChildren().remove(dialog);
            dialog.show(root);
            LoginService loginService = new LoginService();
            loginService.setUsername(inputUsername.getText());
            loginService.setPassword(inputPassword.getText());
            loginService.start();
            loginService.setOnSucceeded(event1 -> {
                dialog.close();
                if (!((Boolean) event1.getSource().getValue())) {
                    JFXAlert alert = new JFXAlert((Stage) btnLogin.getScene().getWindow());
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setOverlayClose(false);
                    JFXDialogLayout layout = new JFXDialogLayout();
                    layout.setHeading(new Label("Login Failed"));
                    layout.setBody(new Label("Please check your username and password."));
                    JFXButton closeButton = new JFXButton("Close");
                    closeButton.getStyleClass().add("dialog-accept");
                    closeButton.setOnAction(event2 -> alert.hideWithAnimation());
                    layout.setActions(closeButton);
                    alert.setContent(layout);
                    alert.show();
                } else {

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    try {
                        Parent parent = fxmlLoader.load(getClass().getResourceAsStream("/fxml/main.fxml"));
                        Stage stage = new Stage();
                        Scene scene = new Scene(parent);
                        scene.getStylesheets().add(getClass().getResource("/css/main.css").toExternalForm());
                        stage.setScene(scene);
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                    btnLogin.getScene().getWindow().hide();
                }
            });
        });
        linkAuthor.setOnMouseClicked(event -> {
            openWebpage("https://github.com/stevenpowered");
        });
        linkSource.setOnMouseClicked(event -> {
            openWebpage("https://github.com/stevenpowered/instagram-unfollower");
        });
    }

    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean openWebpage(String url) {
        try {
            return openWebpage(new URL(url).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
