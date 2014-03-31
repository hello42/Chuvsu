package com.ulop.chuvsu.app;

import com.ulop.newscardlist.dummy.NewsCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ulop on 12.03.14.
 */
public class FeedParser {
    public ArrayList<NewsCardAdapter.NewsCard> parse(InputStream in)
            throws IOException {
        ArrayList<NewsCardAdapter.NewsCard> newsList = new ArrayList<NewsCardAdapter.NewsCard>();
        try {

            JSONArray jsonArray = getJSONArray(in);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                NewsCardAdapter.NewsCard newsCard = new NewsCardAdapter.NewsCard(object.getString("title"),
                        object.getString("body"), "000", 0);
                newsCard.id = object.getString("id");
                newsList.add(0, newsCard);
            }

           return newsList;

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }

        return newsList;
    }

    public JSONArray getJSONArray(InputStream in) throws IOException{
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
            try{if(in != null)in.close();}catch(Exception squish){}
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
