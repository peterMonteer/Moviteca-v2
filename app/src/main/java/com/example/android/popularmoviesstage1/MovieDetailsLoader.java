package com.example.android.popularmoviesstage1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by pedro on 29/06/2018.
 */

public class MovieDetailsLoader extends AsyncTaskLoader {

    String trailerUrl;
    String reviewUrl;

    public MovieDetailsLoader(Context context, String trailerUrl, String reviewUrl){
        super(context);
        this.trailerUrl = trailerUrl;
        this.reviewUrl = reviewUrl;
    }

    @Override
    protected void onStartLoading() {
        Log.i("Order", "onStartLoading");
        forceLoad();
    }

    @Override
    public MovieDetails loadInBackground() {
        String trailerJsonResponse = "";
        String reviewJsonResponse = "";

        URL trailerUrl = QueryUtils.createUrl(this.trailerUrl);
        URL reviewUrl = QueryUtils.createUrl(this.reviewUrl);
        try {
            trailerJsonResponse=QueryUtils.makeHttpRequest(trailerUrl);
            reviewJsonResponse = QueryUtils.makeHttpRequest(reviewUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MovieDetails movie = QueryUtils.extractMovieDetails(trailerJsonResponse, reviewJsonResponse);
        return movie;
    }
}
