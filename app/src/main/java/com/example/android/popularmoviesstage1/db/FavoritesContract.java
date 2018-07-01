package com.example.android.popularmoviesstage1.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by pedro on 29/06/2018.
 */

public class FavoritesContract {

    public final static String AUTHORITY = "com.example.android.popularmoviesstage1";
    public final static Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoritesEntry implements BaseColumns{

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_NAME = "movieName";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_VOTE_AVERAGE = "voteAverage";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POSTER_PATH = "posterPath";

    }
}
