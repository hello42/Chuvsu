package com.ulop.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ulop on 01.08.14.
 */
@Table(name = "organization", id = "_id")
public class Organization extends Model{
    @Column(name = "organizationID")
    public int organizationID;

    @Column(name = "section")
    public String section;

    @Column(name = "name")
    public String name;

    @Column(name = "body")
    public String body;
}
