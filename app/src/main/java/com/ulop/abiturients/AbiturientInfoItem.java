package com.ulop.abiturients;

/**
 * Created by ulop on 19.07.14.
 */
public class AbiturientInfoItem {
    public int id;
    public String title;
    public String body;
    public String url;
    public String image;
    public boolean notification;

    public AbiturientInfoItem(int id, String title, String body, String url, String image) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.url = url;
        this.image = image;
    }

    public AbiturientInfoItem(String title, String body) {
        this.title = title;
        this.body = body;

    }
}
