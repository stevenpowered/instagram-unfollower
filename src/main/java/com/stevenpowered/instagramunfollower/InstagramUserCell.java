package com.stevenpowered.instagramunfollower;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import org.brunocvcunha.instagram4j.requests.payload.InstagramUserSummary;

import java.io.IOException;

public class InstagramUserCell extends ListCell<InstagramUserSummary> {

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private ImageView imageView;

    @FXML
    private GridPane gridPane;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(InstagramUserSummary profile, boolean empty) {
        super.updateItem(profile, empty);

        if(empty || profile == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/instagram_user_cell.fxml"));
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            label1.setText(String.valueOf(profile.getUsername()));
            label2.setText(profile.getFull_name());

            Image image = new Image(profile.getProfile_pic_url(), 30, 30, true, true);
            imageView.setImage(image);

            setText(null);
            setGraphic(gridPane);
        }

    }

}
