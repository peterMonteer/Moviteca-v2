package com.example.android.popularmoviesstage1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pedro on 29/06/2018.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    public FavoritesDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITES_TABLE =
                "CREATE TABLE " + FavoritesContract.FavoritesEntry.TABLE_NAME
                        + " (" + FavoritesContract.FavoritesEntry._ID
                        + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID
                        + " INTEGER NOT NULL UNIQUE, "
                        + FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME
                        + " TEXT NOT NULL, " +
                        FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH
                        + " TEXT NOT NULL, " +
                        FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE +
                        " TEXT NOT NULL, " +
                        FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE +
                        " TEXT NOT NULL, "+
                        FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW +
                        " TEXT NOT NULL" +
                        "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
