package com.greengrass.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by greengrass on 28.07.14.
 */
@Table(name = "phones", id = "_id")
public class Phone extends Model {
    @Column(name = "phone_id")
    public Integer phone_id;

    @Column(name = "title")
    public String title;

    @Column(name = "number")
    public String phone_number;

    public Phone(){
        super();
    }
}
