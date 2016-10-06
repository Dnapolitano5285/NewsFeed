package com.mediocremidgardian.newsfeed;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by Valhalla on 9/10/16.
 */

public class ArticleLoader extends AsyncTaskLoader {

    private static final String LOG_TAG = ArticleLoader.class.getName();

    private List<Article> articles;
    URL mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);

        try {
            mUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Object loadInBackground() {

        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if(activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()) {
            String jsonResponse = "";
            jsonResponse = makeHttpRequest(mUrl);

            articles = QueryUtils.parseJSON(jsonResponse);
        }

        return articles;
    }

    private String makeHttpRequest(URL url) {

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG,"Error making connection",e);
            jsonResponse=null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing connection", e);
                }
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

}
