package com.example.android.popularmoviesstage1;

/**
 * Created by pedro on 29/06/2018.
 */

public class MovieDetails {

    private String youtubeKey;
    private String reviewAuthor;
    private String reviewContent;

    public MovieDetails(){
    }

    public void setYoutubeKey(String youtubeKey){
        this.youtubeKey = youtubeKey;
    }
    public String getYoutubeKey(){
        return youtubeKey;
    }

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
