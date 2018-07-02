package com.example.android.popularmoviesstage1;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pedro on 31/03/2018.
 */

public final class QueryUtils {
    private QueryUtils() {
    }

    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("CreateUrl", "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse="";
        HttpURLConnection urlConnection=null;
        InputStream inputStream=null;
        if (url==null){
            return jsonResponse;
        }

        try {
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode()==200){
                inputStream=urlConnection.getInputStream();
                jsonResponse=readFromStream(inputStream);
            } else{
                Log.e("Error Code", String.valueOf(urlConnection.getResponseCode()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            if (inputStream!=null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Return a list of {@link Movie} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Movie> extractMovie(String jsonResponse) {

        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Movie> movies = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject reader = new JSONObject(jsonResponse);
            JSONArray results = reader.getJSONArray("results");
            for ( int i = 0 ; i < results.length() ; i++ ){
                JSONObject currentResult = results.getJSONObject(i);
                String poster_path = currentResult.optString("poster_path");
                String overview = currentResult.optString("overview");
                String release_date = currentResult.optString("release_date");
                int id = currentResult.optInt("id");
                String original_title = currentResult.optString("original_title");
                Double vote_average = currentResult.optDouble("vote_average");

                movies.add(new Movie( poster_path,  overview,  release_date,  id,  original_title,
                 vote_average));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return movies;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
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

    public static MovieDetails extractMovieDetails(String trailerJsonResponse, String reviewJsonResponse){

        if (TextUtils.isEmpty(trailerJsonResponse)) {
            return null;
        }

        MovieDetails movieDetails = new MovieDetails();
        ArrayList<String> movieTrailersKey = new ArrayList<>();
        ArrayList<UserReviews> movieReviews = new ArrayList<>();

        try {
            JSONObject reader = new JSONObject(trailerJsonResponse);
            JSONArray results = reader.getJSONArray("results");
            for ( int i = 0 ; i < results.length() ; i++ ) {
                JSONObject currentResult = results.getJSONObject(i);
                movieTrailersKey.add(currentResult.getString("key"));
            }
            movieDetails.setYoutubeKeys(movieTrailersKey);
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        if (TextUtils.isEmpty(reviewJsonResponse)) {
            return null;
        }
        try {
            JSONObject reader = new JSONObject(reviewJsonResponse);
            JSONArray results = reader.getJSONArray("results");
            for ( int i = 0 ; i < results.length() ; i++ ) {
                JSONObject currentResult = results.getJSONObject(i);
                String author = currentResult.getString("author");
                String content = currentResult.getString("content");
                UserReviews userReviews = new UserReviews(author,content);
                movieReviews.add(userReviews);
            }
            movieDetails.setUserReviews(movieReviews);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movieDetails;

    }
}
