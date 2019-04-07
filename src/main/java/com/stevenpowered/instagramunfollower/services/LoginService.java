package com.stevenpowered.instagramunfollower.services;

import com.stevenpowered.instagramunfollower.InstagramUnfollower;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.brunocvcunha.instagram4j.requests.payload.InstagramLoginResult;

import java.io.IOException;

public class LoginService extends Service<Boolean> {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<Boolean>() {
            @Override
            protected Boolean call() {
                try {
                    InstagramLoginResult setup = InstagramUnfollower.getInstance().setup(username, password);
                    return !setup.getStatus().equals("fail");
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
    }

}
