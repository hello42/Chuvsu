package com.ulop.parsers;

import com.ulop.newscardlist.dummy.NewsCardAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ulop on 12.03.14.
 */
public class FeedParser extends BaseParser{
    public ArrayList<NewsCardAdapter.NewsCard> parse(InputStream in)
            throws IOException {
        ArrayList<NewsCardAdapter.NewsCard> newsList = new ArrayList<NewsCardAdapter.NewsCard>();
        try {

            JSONArray jsonArray = getJSONArray(in);

            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                NewsCardAdapter.NewsCard newsCard = new NewsCardAdapter.NewsCard(object.getString("title"),
                        object.getString("body"), object.getString("updated_at"), "");
                newsCard.image = object.getString("image");
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

}
