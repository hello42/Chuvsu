package com.greengrass.parsers;

import android.util.Log;

import com.greengrass.models.Faculty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by greengrass on 01.07.14.
 */
public class FacultyInfoParser extends BaseParser{


    public List<Faculty> getAsModelList(InputStream inputStream){
        List<Faculty> fList = new ArrayList<Faculty>();
        try {
            JSONArray jArray = getJSONArray(inputStream);
	        generateModelList(fList, jArray);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fList;
    }

	public List<Faculty> getAsModelList(JSONArray jArray){
		List<Faculty> fList = new ArrayList<Faculty>();
		try {
			generateModelList(fList, jArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return fList;
	}

	private void generateModelList(List<Faculty> fList, JSONArray jArray) throws JSONException {
		for (int i = 0; i < jArray.length(); i++) {
		    JSONObject object = jArray.getJSONObject(i);

		    Faculty faculty = new Faculty();
		    faculty.faculty_name = object.getString("name");
		    faculty.info = object.getString("info");
		    faculty.faculty_id = object.getString("id");
		    faculty.logo = object.getString("logo");
		    faculty.url = object.getString("url");
		    Log.i(TAG, faculty.faculty_name);
		    fList.add(faculty);
		}
	}

}
