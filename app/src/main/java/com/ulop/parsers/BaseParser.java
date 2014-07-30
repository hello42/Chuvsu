package com.ulop.parsers;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ulop on 19.07.14.
 */
public class BaseParser {

    String TAG;

    public BaseParser() {
        this.TAG = this.getClass().getName();
    }

    JSONArray getJSONArray(InputStream in) throws IOException {
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
            try{
                in.close();
            }catch(Exception ignored){}
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
