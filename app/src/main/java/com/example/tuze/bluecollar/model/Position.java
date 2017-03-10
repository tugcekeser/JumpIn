package com.example.tuze.bluecollar.model;

import org.parceler.Parcel;

/**
 * Created by tugce.
 */

@Parcel
public class Position {
    private String positionReference;
    private String companyName;
    private String title;
    private String deadline;
    private String description;
    private String location;
    private String salary;
    private String imageLink;

    public Position() {
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public void setPositionReference(String positionReference) {
        this.positionReference = positionReference;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDescription() {
        if (description != null)
            return description;
        else
            return "";
    }

    public String getLocation() {
        if (location != null)
            return location;
        else
            return "";
    }

    public String getSalary() {
        if (salary != null)
            return salary;
        else
            return "";
    }

    public String getCompanyName() {
        if (companyName != null)
            return companyName;
        else
            return "";
    }

    public String getTitle() {
        if (title != null)
            return title;
        else
            return "";
    }

    public String getPositionReference() {
        if (positionReference != null)
            return positionReference;
        else
            return "";
    }

    public String getImageLink() {
        return imageLink;
    }
}
