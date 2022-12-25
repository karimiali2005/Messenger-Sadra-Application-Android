package com.Sadraafzar.Messenger.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 3/27/2018.
 */

public class UserViewModel {
    @SerializedName("UserID")
    public int UserID ;
    @SerializedName("UserName")
    public String UserName ;
    @SerializedName("FirstName")
    public String FirstName ;
    @SerializedName("LastName")
    public String LastName ;
    @SerializedName("UserType")
    public int UserType;
    @SerializedName("Address")
    public String Address;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
    }
}
