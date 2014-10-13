package com.greengrass.parsers;

import android.util.Log;

import com.greengrass.models.Organization;

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
public class OrganizationParser extends BaseParser {
    public List<Organization> getAsModelList(InputStream inputStream){
        List<Organization> oList = new ArrayList<Organization>();
        try {
            JSONArray jArray = getJSONArray(inputStream);
	        generateList(oList, jArray);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return oList;
    }

	public List<Organization> getAsModelList(JSONArray jArray){
		List<Organization> oList = new ArrayList<Organization>();
		try {
			generateList(oList, jArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return oList;
	}

	private void generateList(List<Organization> oList, JSONArray jArray) throws JSONException {
		for (int i = 0; i < jArray.length(); i++) {
		    JSONObject object = jArray.getJSONObject(i);
		    Organization organization = new Organization();
		    organization.organizationID = object.getInt("id");
		    organization.name = object.getString("name");
		    organization.body = object.getString("body");
		    organization.section = object.getString("typeof");
		    oList.add(organization);
		}
	}
}
