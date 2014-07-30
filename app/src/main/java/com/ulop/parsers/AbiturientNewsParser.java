package com.ulop.parsers;

import android.util.Log;

import com.ulop.models.AbiturientNews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ulop on 20.07.14.
 */
public class AbiturientNewsParser extends BaseParser {

    public List<AbiturientNews> getAsModelList(InputStream inputStream){
        List<AbiturientNews> eList = new ArrayList<AbiturientNews>();
        try {
            JSONArray jArray = getJSONArray(inputStream);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject object = jArray.getJSONObject(i);
                AbiturientNews aNew = new AbiturientNews();
                aNew.news_id = object.getInt("id");
                aNew.title = object.getString("title");
                aNew.content = object.getString("body");
                aNew.image = object.getString("img");
                aNew.published = object.getString("updated_at");
                aNew.notificate = object.getString("notification");
                eList.add(aNew);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return eList;
    }
    
}
