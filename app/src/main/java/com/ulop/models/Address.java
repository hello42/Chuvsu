package com.ulop.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ulop on 30.07.14.
 */
@Table(name = "address", id = "_id")
public class Address extends Model {
    @Column(name = "address_id")
    public Integer address_id;

    @Column(name = "title")
    public String title;

    @Column(name = "address")
    public String address;

    @Column(name = "image")
    public String image;

    @Column(name = "coordinates")
    public String coordinates;

    public Address() {
       super();
    }
}
