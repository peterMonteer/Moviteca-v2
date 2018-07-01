package com.example.android.popularmoviesstage1.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.popularmoviesstage1.db.FavoritesContract.FavoritesEntry.CONTENT_URI;
import static com.example.android.popularmoviesstage1.db.FavoritesContract.FavoritesEntry.TABLE_NAME;

/**
 * Created by pedro on 30/06/2018.
 */

public class FavoritesContentProvider extends ContentProvider {

    private FavoritesDbHelper favoritesDbHelper;

    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES, FAVORITES);
        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES + "/#", FAVORITES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        favoritesDbHelper = new FavoritesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        final SQLiteDatabase db = favoritesDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch (match){
            case FAVORITES:
                returnCursor = db.query(TABLE_NAME,strings,s,strings1,null,null,s1);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return the desired Cursor
        return returnCursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = favoritesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match){
            case FAVORITES:
                long id = db.insert(TABLE_NAME,null,contentValues);
                if (id >0){
                    returnUri = ContentUris.withAppendedId(CONTENT_URI,id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = favoritesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int favoritesDeleted;

        switch (match) {

            case FAVORITES:
                favoritesDeleted = db.delete(TABLE_NAME, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (favoritesDeleted != 0) {
            // A task was deleted, set notification
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return favoritesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
