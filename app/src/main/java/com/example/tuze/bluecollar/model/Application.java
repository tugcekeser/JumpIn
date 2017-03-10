package com.example.tuze.bluecollar.model;

import org.parceler.Parcel;

/**
 * Created by tugce.
 */

@Parcel
public class Application {
    private String userId;
    private String positionReference;
    private String applicationDate;

    public Application() {

    }

    public void setPositionReference(String positionReference) {
        this.positionReference = positionReference;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPositionReference() {
        if (positionReference != null)
            return positionReference;
        else
            return "";
    }

    public String getApplicationDate() {
        if (applicationDate != null)
            return applicationDate;
        else
            return "";
    }

    public String getUserId() {
        return userId;
    }

}
