package com.ulop.parsers;

import android.util.Log;

import com.ulop.models.Faculty;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ulop on 01.07.14.
 */
public class FacultyInfoParser extends BaseParser{


    public List<Faculty> getAsModelList(InputStream inputStream){
        List<Faculty> fList = new ArrayList<Faculty>();
        try {
            JSONArray jArray = getJSONArray(inputStream);
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
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return fList;
    }

}
