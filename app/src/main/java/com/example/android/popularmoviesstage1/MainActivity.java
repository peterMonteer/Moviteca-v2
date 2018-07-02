package com.example.android.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.popularmoviesstage1.db.FavoritesContract;
import com.example.android.popularmoviesstage1.db.FavoritesDbHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public final static String BASE_URL= "https://api.themoviedb.org/3/movie";
    public final static String API_KEY = "";
    private static final int API_LOADER = 1;
    private static final int DATABASE_LOADER = 2;
    private String displayBy;

    private LoaderManager.LoaderCallbacks<List<Movie>> movieListLoaderListener
            = new LoaderManager.LoaderCallbacks<List<Movie>>() {

        @Override
        public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
            Uri baseUri = Uri.parse(BASE_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            uriBuilder.appendPath(displayBy).appendQueryParameter("api_key",API_KEY).appendQueryParameter("language","en-US").appendQueryParameter("page","2");

            return new MovieLoader(getBaseContext() , uriBuilder.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
            movieAdapter.clear();
            if (movies !=null && !movies.isEmpty()){
                movieAdapter.addAll(movies);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> favoriteDatabaseLoaderListener
            = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new DatabaseLoader(getBaseContext());
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            displayFavorites(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    GridView gridView;
    MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridview);

        movieAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        gridView.setAdapter(movieAdapter);

        setupSharedPreferences();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie currentMovie = (Movie) gridView.getAdapter().getItem(i);
                launchDetailActivity(currentMovie);

            }
        });

    }

    public void launchDetailActivity(Movie currentMovie){
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra(MovieDetailsActivity.EXTRA_POSITION, currentMovie);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayFavorites(Cursor cursor){

        ArrayList<Movie> movies = new ArrayList<>();

        while (cursor.moveToNext()){
            String posterPath = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH));
            String overview = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW));
            String release_date = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE));
            int id = cursor.getInt(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID));
            String original_title = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME));
            Double vote_average = cursor.getDouble(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE));
            Log.d("cursormove",posterPath);

            movies.add(new Movie(posterPath,overview,release_date,id,original_title,vote_average));
            Log.d("cursormove", movies.get(0).toString());
        }

        movieAdapter.clear();
        if (movies !=null && !movies.isEmpty()){
            movieAdapter.addAll(movies);

        }

        cursor.close();
    }

    private void setupSharedPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        displayBy = sharedPreferences.getString("pref_sort_movies","top_rated");
        loadMainView();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_sort_movies")) {
            displayBy = sharedPreferences.getString(key,null);
            loadMainView();
        }
    }

    public void loadMainView(){
        if (!displayBy.equals("favorites")){
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                getLoaderManager().restartLoader(1, null, movieListLoaderListener);
            }
        } else {
            Log.d("displayFavoritecall", displayBy);
            getLoaderManager().restartLoader(2,null,favoriteDatabaseLoaderListener);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
