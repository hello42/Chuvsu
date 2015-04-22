package com.greengrass.parsers;

import android.util.Log;

import com.greengrass.models.Announcement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ulop on 11.09.14.
 */
public class AnnouncementParser extends BaseParser {
	public List<Announcement> getAsModelList(InputStream inputStream){
		List<Announcement> eList = new ArrayList<Announcement>();
		try {
			JSONArray jArray = getJSONArray(inputStream);
			for (int i = 0; i < jArray.length(); i++) {
				JSONObject object = jArray.getJSONObject(i);
				Announcement announcement = new Announcement();
				announcement.announcement_id = object.getInt("id");
				announcement.title = object.getString("title");
				announcement.body = object.getString("body");
				announcement.date = object.getString("date");
				announcement.notification = object.getBoolean("notification");
				eList.add(announcement);
			}
		} catch (IOException e) {
			//e.printStackTrace();
			Log.i(TAG, "Failed to parse jsonArray");
		} catch (JSONException e) {
			//e.printStackTrace();
			Log.i(TAG, "Failed to parse jsonArray");

		}

		return eList;
	}

	public List<Announcement> getAsModelList(JSONArray jArray){
		List<Announcement> fList = new ArrayList<Announcement>();
		try {
			generateModelList(fList, jArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return fList;
	}

	private void generateModelList(List<Announcement> fList, JSONArray jArray) throws JSONException {
		for (int i = 0; i < jArray.length(); i++) {
			JSONObject object = jArray.getJSONObject(i);

			Announcement announcement = new Announcement();
			announcement.announcement_id = object.getInt("id");
			announcement.title = object.getString("title");
			announcement.body = object.getString("body");
			announcement.date = object.getString("date");
			announcement.notification = object.getBoolean("notification");
			//Log.i(TAG, faculty.faculty_name);
			fList.add(announcement);
		}
	}
}
