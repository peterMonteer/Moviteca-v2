package com.example.android.popularmoviesstage1;

import java.util.ArrayList;

/**
 * Created by pedro on 29/06/2018.
 */

public class MovieDetails {

    private ArrayList<String> youtubeKeys;
    private ArrayList<UserReviews> userReviews;

    public MovieDetails(){
    }

    public void setYoutubeKeys(ArrayList<String> youtubeKeys){
        this.youtubeKeys = youtubeKeys;
    }
    public ArrayList<String> getYoutubeKeys(){
        return youtubeKeys;
    }

    public ArrayList<UserReviews> getUserReviews() {
        return userReviews;
    }

    public void setUserReviews(ArrayList<UserReviews> userReviews) {
        this.userReviews = userReviews;
    }
}
