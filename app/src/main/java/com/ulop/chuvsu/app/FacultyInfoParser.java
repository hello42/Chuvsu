package com.ulop.chuvsu.app;

import com.ulop.faculty.FacultyContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ulop on 01.07.14.
 */
public class FacultyInfoParser {
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

    JSONArray getJSONArray(InputStream in) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{if(in != null)in.close();}catch(Exception ignored){}
        }

        StringBuilder stringBuilder = new StringBuilder();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(sb.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //jsonArray.
        return jsonArray;

    }
}
