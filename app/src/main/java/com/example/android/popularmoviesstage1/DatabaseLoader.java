package com.example.android.popularmoviesstage1;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import static com.example.android.popularmoviesstage1.db.FavoritesContract.FavoritesEntry.CONTENT_URI;

/**
 * Created by pedro on 01/07/2018.
 */

public class DatabaseLoader extends AsyncTaskLoader {

    Cursor mFavoriteData = null;

    public DatabaseLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (mFavoriteData != null) {
            deliverResult(mFavoriteData);
        } else {
            forceLoad();
        }
    }
    @Override
    public Cursor loadInBackground() {
        try {
            return getContext().getContentResolver().query(CONTENT_URI,
                    null,
                    null,
                    null,
                    null);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public void deliverResult(Cursor data) {
        mFavoriteData = data;
        super.deliverResult(data);
    }
}
