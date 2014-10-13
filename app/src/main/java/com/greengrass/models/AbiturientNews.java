package com.greengrass.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by greengrass on 29.07.14.
 * comment
 */
@Table(name = "abitnew", id = "_id")
public class AbiturientNews extends Model {
    public AbiturientNews() {
        super();
    }

    @Column(name = "news_id")
    public Integer news_id;

    @Column(name = "title")
    public String title;

    @Column(name = "content")
    public String content;

    @Column(name = "published")
    public String published;

    @Column(name = "image")
    public String image;

    @Column(name = "notificate")
    public String notificate;
}
