package com.greengrass.parsers;

import android.util.Log;

import com.greengrass.models.StaticInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by greengrass on 02.08.14.
 */
public class StaticInfoParser extends BaseParser {
    public List<StaticInfo> getAsModelList(InputStream inputStream){
        List<StaticInfo> iList = new ArrayList<StaticInfo>();
        try {
            JSONArray jArray = getJSONArray(inputStream);
	        generateList(iList, jArray);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return iList;
    }

	public List<StaticInfo> getAsModelList(JSONArray jArray){
        List<StaticInfo> iList = new ArrayList<StaticInfo>();
        try {
	        generateList(iList, jArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return iList;
    }

	private void generateList(List<StaticInfo> iList, JSONArray jArray) throws JSONException {
		for (int i = 0; i < jArray.length(); i++) {
		    JSONObject object = jArray.getJSONObject(i);
		    StaticInfo info = new StaticInfo();
		    info.page_id = object.getInt("id");
		    info.title = object.getString("title");
		    info.body = object.getString("body");
		    info.section = object.getString("typeof");
		    iList.add(info);
		}
	}
}
