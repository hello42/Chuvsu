package com.ulop.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ulop on 01.08.14.
 */
@Table(name = "student_info", id = "_id")
public class InfoForStudent extends Model{
    @Column(name = "infoID")
    public int infoID;

    @Column(name = "section")
    public String section;

    @Column(name = "title")
    public String title;

    @Column(name = "body")
    public String body;
}
