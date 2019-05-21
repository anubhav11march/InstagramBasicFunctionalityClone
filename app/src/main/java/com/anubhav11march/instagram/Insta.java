package com.anubhav11march.instagram;

public class Insta {

    private String title, desc, image, username;

    public Insta(){

    }

    public Insta(String title, String desc, String image){
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.username = username;

    }

    public String getTitle(){
        return title;
    }

    public String getDesc(){
        return desc;
    }

    public  String getImage(){
        return image;
    }

    public String getUsername(){ return username; }

    public void setText(String title ){
        this.title = title;
    }

    public void setDesc (String desc){
        this.desc = desc;
    }

    public void setImage (String image){
        this.image = image;
    }

    public void setUsername (String username) { this.username = username; }

}
