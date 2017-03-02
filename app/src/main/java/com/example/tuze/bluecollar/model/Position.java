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
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getSalary() {
        return salary;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getTitle() {
        return title;
    }

    public String getPositionReference() {
        return positionReference;
    }

    public String getImageLink() {
        return imageLink;
    }
}
