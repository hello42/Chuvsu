package com.ulop.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ulop on 05.08.14.
 */
@Table(name = "pages", id = "_id")
public class StaticInfo extends Model {
    @Column(name = "page_id")
    public int page_id;

    @Column(name = "section")
    public String section;

    @Column(name = "title")
    public String title;

    @Column(name = "body")
    public String body;
}
