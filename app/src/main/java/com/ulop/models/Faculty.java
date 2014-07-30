package com.ulop.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ulop on 28.07.14.
 */
@Table(name = "faculty", id = "_id")
public class Faculty extends Model{
    @Column(name = "faculty_id")
    public String faculty_id;

    @Column(name = "namefct")
    public String faculty_name;

    @Column(name = "logo")
    public String logo;

    @Column(name = "urlfct")
    public String url;

    @Column(name = "info")
    public String info;

    public Faculty() {
        super();
    }
}
