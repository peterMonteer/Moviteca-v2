package com.example.android.popularmoviesstage1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by pedro on 31/03/2018.
 */

public class MovieLoader extends AsyncTaskLoader {

    String url;

    public MovieLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i("Order", "onStartLoading");
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        String jsonResponse = "";

        URL url = QueryUtils.createUrl(this.url);
        try {
            jsonResponse=QueryUtils.makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Movie> movies = QueryUtils.extractMovie(jsonResponse);
        return movies;
    }
}
