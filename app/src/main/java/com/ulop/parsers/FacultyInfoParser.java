package com.ulop.parsers;

import com.ulop.faculty.FacultyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ulop on 01.07.14.
 */
public class FacultyInfoParser extends BaseParser{
    public ArrayList<FacultyContent.FacultyItem> parse(InputStream in)
            throws IOException {
        ArrayList<FacultyContent.FacultyItem> fctList = new ArrayList<FacultyContent.FacultyItem>();
        try {

            JSONArray jsonArray = getJSONArray(in);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                FacultyContent.FacultyItem fctCard = new FacultyContent.FacultyItem(object.getString("name"),
                        object.getString("info"), object.getString("url"), "");
                fctCard.logo = object.getString("logo");
                fctCard.id = object.getString("id");
                fctList.add(0, fctCard);
            }

            return fctList;

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }

        return fctList;
    }

}
