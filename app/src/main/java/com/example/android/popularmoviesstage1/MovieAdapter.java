package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pedro on 31/03/2018.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    Context mContext;

    public MovieAdapter(@NonNull Context context, @NonNull List<Movie> objects) {
        super(context, 0, objects);
        this.mContext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ImageView imageView;
        if (convertView == null){
            imageView = new ImageView(mContext);
            //imageView.setLayoutParams(new ViewGroup.LayoutParams(185,277));
            //imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            //imageView.setPadding(8,8,8,8);
            imageView.setAdjustViewBounds(true);
        }else{
            imageView = (ImageView) convertView;
        }
        Movie currentMovie = getItem(position);
        Picasso.with(mContext).load( "http://image.tmdb.org/t/p/w185/" + currentMovie.getPoster_path()).into(imageView);
        return imageView;
    }
}
