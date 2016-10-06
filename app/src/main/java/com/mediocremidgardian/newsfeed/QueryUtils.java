package com.mediocremidgardian.newsfeed;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Valhalla on 9/10/16.
 */

public final class QueryUtils {

    //Private Constructor so that no one can instantiate an instance. Static helper methods only
    private QueryUtils() {
    }

    public static ArrayList<Article> parseJSON(String data) {

        ArrayList<Article> articles = new ArrayList<>();

        try {

            JSONObject rawData = new JSONObject(data);
            JSONObject response = rawData.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {

                JSONObject article = results.getJSONObject(i);

                String publicationDate = article.getString("webPublicationDate");
                String title = article.getString("webTitle");
                String url = article.getString("webUrl");

                JSONObject fields = article.getJSONObject("fields");
                String byline = fields.getString("byline");

                articles.add(new Article(title, byline, url, publicationDate));
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing JSON", e);
        }

        return articles;
    }

}
