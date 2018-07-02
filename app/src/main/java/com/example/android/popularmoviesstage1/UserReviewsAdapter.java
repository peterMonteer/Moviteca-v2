package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pedro on 01/07/2018.
 */

public class UserReviewsAdapter extends RecyclerView.Adapter {

    private ArrayList<UserReviews> mUserReviewsArray;
    private TextView mAuthorName;
    private TextView mReviewContent;

    public UserReviewsAdapter(ArrayList<UserReviews> mUserReviewsArray){
        this.mUserReviewsArray = mUserReviewsArray;
    }

    @NonNull
    @Override
    public UserReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.single_review;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,parent,false);
        return new UserReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        UserReviews currentReview = mUserReviewsArray.get(position);
        Log.d("reviewarrayy",mUserReviewsArray.get(position).getReviewAuthor());

        mAuthorName = holder.itemView.findViewById(R.id.author_name_tv);
        mReviewContent = holder.itemView.findViewById(R.id.review_tv);

        mAuthorName.setText(currentReview.getReviewAuthor());
        mReviewContent.setText(currentReview.getReviewContent());
    }

    @Override
    public int getItemCount() {

        if (mUserReviewsArray == null){
            return 0;
        }
        return mUserReviewsArray.size();
    }

    public class UserReviewViewHolder extends RecyclerView.ViewHolder{

        public UserReviewViewHolder(View view){
            super(view);

        }
    }
}
