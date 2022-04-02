package com.homework.firebase;

public class Upload {
    private String name;
    private String imageURL;
    private String key;

    public Upload(){
        //need empty constructor
    }

    public Upload(String name, String imageURL) {
        if (name.trim().equals("")){
            name = "No Name";
        }
        this.name = name;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
