package com.greengrass.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by greengrass on 29.07.14.
 */
@Table(name = "entry", id = "_id")
public class Entry extends Model{
    @Column(name = "news_id")
    public String news_id;

    @Column(name = "title")
    public String title;

    @Column(name = "content")
    public String content;

    @Column(name = "published")
    public String published;

    @Column(name = "image")
    public String image;

    public Entry() {
        super();
    }
}
