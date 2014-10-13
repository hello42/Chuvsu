package com.greengrass.parsers;

import android.util.Log;

import com.greengrass.models.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by greengrass on 12.03.14.
 */
public class FeedParser extends BaseParser{

    public List<Entry> getAsModelList(InputStream inputStream){
        List<Entry> eList = new ArrayList<Entry>();
        try {
            JSONArray jArray = getJSONArray(inputStream);
	        generateList(eList, jArray);
        } catch (IOException e) {
            //e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");
        } catch (JSONException e) {
            //e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");

        }

        return eList;
    }

	public List<Entry> getAsModelList(JSONArray jArray){
		List<Entry> eList = new ArrayList<Entry>();
		try {
			generateList(eList, jArray);
		} catch (JSONException e) {
			//e.printStackTrace();
			Log.i(TAG, "Failed to parse jsonArray", e);

		}

		return eList;
	}

	private void generateList(List<Entry> eList, JSONArray jArray) throws JSONException {
		for (int i = 0; i < jArray.length(); i++) {
		    JSONObject object = jArray.getJSONObject(i);
		    Entry entry = new Entry();
		    entry.news_id = object.getString("id");
		    entry.title = object.getString("title");
		    entry.content = object.getString("body");
		    entry.image = object.getString("image");
		    entry.published = object.getString("updated_at");
		    eList.add(entry);
		}
	}


}
