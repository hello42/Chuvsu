package com.ulop.parsers;

import android.util.Log;

import com.activeandroid.Model;
import com.ulop.models.Phone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ulop on 30.07.14.
 */
public class PhoneParser extends BaseParser {
    public List<Phone> getAsModelList(InputStream inputStream){
        List<Phone> pList = new ArrayList<Phone>();
        try {
            JSONArray jArray = getJSONArray(inputStream);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject object = jArray.getJSONObject(i);
                Phone phone = new Phone();
                phone.phone_id = object.getInt("id");
                phone.title = object.getString("title");
                phone.phone_number = object.getString("number");
                pList.add(phone);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pList;
    }
}
