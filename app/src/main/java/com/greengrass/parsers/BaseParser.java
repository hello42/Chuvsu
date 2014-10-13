package com.greengrass.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by greengrass on 19.07.14.
 */
public class BaseParser {

    String TAG;
    private boolean ready;

    public BaseParser() {
        this.TAG = this.getClass().getName();
    }

    public JSONArray getJSONArray(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
	            Log.e(TAG + "+SB", line);
                sb.append(line + "\n");
            }
        } catch (IOException e) {
	        Log.e(TAG, "JSON read exception", e);
        }finally {
            try{
                in.close();
            }catch(Exception ignored){}
        }




	    JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(sb.toString());
        } catch (JSONException e) {
	        Log.e(TAG, "JSONArray parsing exception", e);
        }
        //jsonArray.
        return jsonArray;

    }
}
