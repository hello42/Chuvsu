package com.greengrass.parsers;

import android.util.Log;

import com.greengrass.models.Address;

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
public class AddressParser extends BaseParser {
    public List<Address> getAsModelList(InputStream inputStream){
        List<Address> aList = new ArrayList<Address>();
        try {
            JSONArray jArray = getJSONArray(inputStream);
	        getAsModelList(aList, jArray);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return aList;
    }

	public List<Address> getAsModelList(JSONArray jArray){
        List<Address> aList = new ArrayList<Address>();
        try {
	        getAsModelList(aList, jArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return aList;
    }

	private void getAsModelList(List<Address> aList, JSONArray jArray) throws JSONException {
		for (int i = 0; i < jArray.length(); i++) {
		    JSONObject object = jArray.getJSONObject(i);
		    Address address = new Address();
		    address.address_id = object.getInt("id");
		    address.title = object.getString("title");
		    address.image = object.getString("image");
		    address.address = object.getString("address");
		    address.coordinates = object.getString("coord");
		    aList.add(address);
		}
	}
}
