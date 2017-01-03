package com.example.tuze.bluecollar.model;

/**
 * Created by tuze on 11/25/16.
 */

public class Application {
    private String userId;
    private String positionReference;
    private String applicationDate;

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
        return positionReference;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public String getUserId() {
        return userId;
    }

}
