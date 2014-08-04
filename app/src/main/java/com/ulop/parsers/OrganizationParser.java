package com.ulop.parsers;

import android.util.Log;

import com.ulop.models.Organization;
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
public class OrganizationParser extends BaseParser {
    public List<Organization> getAsModelList(InputStream inputStream){
        List<Organization> oList = new ArrayList<Organization>();
        try {
            JSONArray jArray = getJSONArray(inputStream);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject object = jArray.getJSONObject(i);
                Organization organization = new Organization();
                organization.organizationID = object.getInt("id");
                organization.name = object.getString("name");
                organization.body = object.getString("body");
                organization.section = object.getString("typeof");
                oList.add(organization);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Failed to parse jsonArray");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return oList;
    }
}
