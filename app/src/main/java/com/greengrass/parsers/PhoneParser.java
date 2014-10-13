package com.greengrass.parsers;

import android.util.Log;

import com.greengrass.models.Phone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by greengrass on 30.07.14.
 */
public class PhoneParser extends BaseParser {
    public List<Phone> getAsModelList(InputStream inputStream){
        List<Phone> pList = new ArrayList<Phone>();
        try {
            JSONArray jArray = getJSONArray(inputStream);
	        generateList(pList, jArray);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pList;
    }

	public List<Phone> getAsModelList(JSONArray jArray){
        List<Phone> pList = new ArrayList<Phone>();
        try {
	        generateList(pList, jArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pList;
    }

	private void generateList(List<Phone> pList, JSONArray jArray) throws JSONException {
		for (int i = 0; i < jArray.length(); i++) {
		    JSONObject object = jArray.getJSONObject(i);
		    Phone phone = new Phone();
		    phone.phone_id = object.getInt("id");
		    phone.title = object.getString("title");
		    phone.phone_number = object.getString("number");
		    pList.add(phone);
		}
	}
}
