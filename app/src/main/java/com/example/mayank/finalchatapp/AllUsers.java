package com.example.mayank.finalchatapp;

/**
 * Created by mayank on 10/1/18.
 */

public class AllUsers {

    private String name;
    private String thumImage;
    private String status;


    public AllUsers(){

    }

    public String getThumImage() {
        return thumImage;
    }

    public AllUsers(String name, String image, String status) {
        this.name = name;
        this.thumImage = image;
        this.status = status;
    }

    public void setThumImage(String thumImage) {
        this.thumImage = thumImage;
    }

    public void setName(String name) { this.name = name; }

    public void setImage(String image) {
        this.thumImage = image;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    public String getName() {
        return name;
    }


    public String getStatus() {
        return status;
    }

}
