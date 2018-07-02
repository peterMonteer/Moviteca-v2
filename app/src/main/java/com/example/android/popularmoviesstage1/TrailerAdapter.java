package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by pedro on 02/07/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter {

    private ArrayList<String> youtubeTrailerKeys;
    private Context context;
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch";

    public TrailerAdapter(ArrayList<String> youtubeTrailerKeys){
        this.youtubeTrailerKeys = youtubeTrailerKeys;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem,parent,false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Button trailerButton = (Button) holder.itemView;
        trailerButton.setText("Trailer " + (position+1));
        Uri youtubeBaseUri = Uri.parse(YOUTUBE_BASE_URL);
        final Uri.Builder youtubeUriBuilder = youtubeBaseUri.buildUpon();
        youtubeUriBuilder.appendQueryParameter("v",youtubeTrailerKeys.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                trailerIntent.setData(youtubeUriBuilder.build());
                startActivity(context,trailerIntent,null);            }
        });
    }

    @Override
    public int getItemCount() {
        if (youtubeTrailerKeys == null){
            return 0;
        }
        return youtubeTrailerKeys.size();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder{

        public TrailerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
