package com.stevenpowered.instagramunfollower;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.*;
import org.brunocvcunha.instagram4j.requests.payload.InstagramGetUserFollowersResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainController implements Initializable {

    public JFXListView<InstagramUserSummary> listView;
    public ImageView profileImage;
    public JFXButton btnUnfollow;
    public GridPane gridPane;

    private List<InstagramUserSummary> followers = new ArrayList<>();
    private List<InstagramUserSummary> following;
    private List<InstagramUserSummary> notFollowingBack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            initData();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        gridPane.setVisible(false);

        listView.setCellFactory(listView -> new InstagramUserCell());
        notFollowingBack.forEach(listView.getItems()::add);

        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                gridPane.setVisible(false);
            } else {
                gridPane.setVisible(true);
                showProfile(newValue);
            }
        });
    }

    private void showProfile(InstagramUserSummary newValue) {
        profileImage.setImage(new Image(newValue.getProfile_pic_url()));
        btnUnfollow.setOnMouseClicked(event -> {
            Instagram4j instagram = InstagramUnfollower.getInstance().getInstagram();
            try {
                instagram.sendRequest(new InstagramUnfollowRequest(newValue.getPk()));
                listView.getItems().remove(newValue);
            } catch (IOException e) {
                alertFailure();
                e.printStackTrace();
            }
        });
    }

    private void alertFailure() {
        JFXAlert alert = new JFXAlert((Stage) btnUnfollow.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("Unfollow Failed"));
        layout.setBody(new Label("Failed to unfollow the user. Please check your internet connection."));
        JFXButton closeButton = new JFXButton("Close");
        closeButton.getStyleClass().add("dialog-accept");
        closeButton.setOnAction(event2 -> alert.hideWithAnimation());
        layout.setActions(closeButton);
        alert.setContent(layout);
        alert.show();
    }

    private void initData() throws IOException {
        Instagram4j instagram = InstagramUnfollower.getInstance().getInstagram();

        String nextMaxId = null;
        while (true) {
            InstagramGetUserFollowersResult fr = instagram.sendRequest(new InstagramGetUserFollowersRequest(instagram.getUserId(), nextMaxId));
            List<InstagramUserSummary> users = fr.getUsers();
            followers.addAll(users);
            nextMaxId = fr.getNext_max_id();

            if (nextMaxId == null) {
                break;
            }
        }

        InstagramGetUserFollowersResult followingRequest = instagram.sendRequest(new InstagramGetUserFollowingRequest(instagram.getUserId()));
        following = followingRequest.getUsers();
        List<String> followersId = followers.stream().map(InstagramUserSummary::getUsername).collect(Collectors.toList());
        notFollowingBack = following.stream().filter(instagramUserSummary -> {
            if (!followersId.contains(instagramUserSummary.getUsername())) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
    }

}
