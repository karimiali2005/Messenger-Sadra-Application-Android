package com.Sadraafzar.Messenger.Image;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shaon on 12/3/2016.
 */

public class ServerResponseUpload {
    boolean success;
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public boolean getSuccess() {
        return success;
    }


}