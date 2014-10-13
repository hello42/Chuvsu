package com.greengrass.parsers;

import android.util.Log;

import com.greengrass.models.InfoForStudent;

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
public class InfoForStudentParser extends BaseParser {
    public List<InfoForStudent> getAsModelList(InputStream inputStream){
        List<InfoForStudent> iList = new ArrayList<InfoForStudent>();
        try {
            JSONArray jArray = getJSONArray(inputStream);
	        geneateList(iList, jArray);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return iList;
    }

	public List<InfoForStudent> getAsModelList(JSONArray jArray){
		List<InfoForStudent> iList = new ArrayList<InfoForStudent>();
		try {
			geneateList(iList, jArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return iList;
	}

	private void geneateList(List<InfoForStudent> iList, JSONArray jArray) throws JSONException {
		for (int i = 0; i < jArray.length(); i++) {
		    JSONObject object = jArray.getJSONObject(i);
		    InfoForStudent info = new InfoForStudent();
		    info.infoID = object.getInt("id");
		    info.title = object.getString("title");
		    info.body = object.getString("body");
		    info.section = object.getString("typeof");
		    iList.add(info);
		}
	}
}
