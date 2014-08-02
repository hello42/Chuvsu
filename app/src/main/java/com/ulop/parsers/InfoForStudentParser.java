package com.ulop.parsers;

import android.util.Log;

import com.ulop.models.InfoForStudent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ulop on 02.08.14.
 */
public class InfoForStudentParser extends BaseParser {
    public List<InfoForStudent> getAsModelList(InputStream inputStream){
        List<InfoForStudent> iList = new ArrayList<InfoForStudent>();
        try {
            JSONArray jArray = getJSONArray(inputStream);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject object = jArray.getJSONObject(i);
                InfoForStudent info = new InfoForStudent();
                info.infoID = object.getInt("id");
                info.title = object.getString("title");
                info.body = object.getString("body");
                info.section = object.getString("typeof");
                iList.add(info);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return iList;
    }
}
