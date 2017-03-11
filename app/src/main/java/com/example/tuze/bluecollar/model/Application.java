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
    private int status;
    private String applicationReference;

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

    public void setApplicationReference(String applicationReference) {
        this.applicationReference = applicationReference;
    }

    public void setStatus(int status) {
        if(status==0)
            this.status = 1;
        else {
            this.status = status;
        }
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

    public int getStatus() {
        return status;
    }

    public String getApplicationReference() {
        return applicationReference;
    }

    public String getUserId() {
        return userId;
    }

}
