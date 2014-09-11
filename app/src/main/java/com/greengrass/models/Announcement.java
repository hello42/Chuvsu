package com.greengrass.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ulop on 11.09.14.
 */
@Table(name = "announcement", id = "_id")

public class Announcement extends Model {
	@Column(name = "announcement_id")
	public Integer announcement_id;

	@Column(name = "title")
	public String title;

	@Column(name = "date")
	public String date;

	@Column(name = "body")
	public String body;

	@Column(name = "notificate")
	public boolean notification;
}
