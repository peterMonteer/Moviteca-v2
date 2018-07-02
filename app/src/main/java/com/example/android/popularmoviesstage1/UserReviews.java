package com.example.android.popularmoviesstage1;

/**
 * Created by pedro on 01/07/2018.
 */

public class UserReviews {

    private String reviewAuthor;
    private String reviewContent;

    public UserReviews(String reviewAuthor,String reviewContent){
        this.reviewAuthor = reviewAuthor;
        this.reviewContent = reviewContent;
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
