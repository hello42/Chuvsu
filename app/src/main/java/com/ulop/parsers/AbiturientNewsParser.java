package com.ulop.parsers;

import com.ulop.abiturients.AbiturientInfoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ulop on 20.07.14.
 */
public class AbiturientNewsParser extends BaseParser {
    public ArrayList<AbiturientInfoItem> parse(InputStream in)
            throws IOException {
        ArrayList<AbiturientInfoItem> newsList = new ArrayList<AbiturientInfoItem>();
        try {

            JSONArray jsonArray = getJSONArray(in);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                AbiturientInfoItem aNew = new AbiturientInfoItem(object.getString("title"), object.getString("body"));
                aNew.url = object.getString("url");
                aNew.image = object.getString("img");
                aNew.id = object.getInt("id");
                aNew.notification = object.getInt("notification");
               // aNew.notification = (aBoolean) ? 1 : 0;
                aNew.publicTime = object.getString("updated_at");
                newsList.add(0, aNew);
            }

            return newsList;

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }

        return newsList;
    }
}
