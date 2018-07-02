package com.example.android.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.db.FavoritesContract;
import com.example.android.popularmoviesstage1.db.FavoritesDbHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.popularmoviesstage1.db.FavoritesContract.FavoritesEntry.CONTENT_URI;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieDetails> {

    public static final String EXTRA_POSITION = "extra_position";
    public static final String BASE_URL = MainActivity.BASE_URL;
    public static final String VIDEO_ENDPOINT = "videos";
    public static final String REVIEWS_ENDPOINT = "reviews";
    public static final String API_KEY = MainActivity.API_KEY;

    private Button trailerButton;
    private TextView reviewContentTextView;
    private CheckBox favoriteCheckBox;
    private RecyclerView userReviewRecyclerView;
    private RecyclerView trailerRecyclerView;

    //Movie variables
    private int id;
    private String movieName;
    private String userRating;
    private String releaseDate;
    private String overView;
    private String posterPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Bundle data = getIntent().getExtras();
        Movie currentMovie = data.getParcelable(EXTRA_POSITION);

        ImageView poster_path = findViewById(R.id.poster_thumbnail);
        TextView original_title = findViewById(R.id.original_title_content);
        TextView user_rating = findViewById(R.id.user_rating_content);
        TextView release_date = findViewById(R.id.release_date_content);
        TextView overview = findViewById(R.id.overview_content);
        favoriteCheckBox = findViewById(R.id.favorite_star);
        userReviewRecyclerView = findViewById(R.id.user_reviews_rv);
        trailerRecyclerView = findViewById(R.id.trailer_rv);

        id = currentMovie.getId();
        movieName = currentMovie.getOriginal_title();
        userRating = currentMovie.getVote_average().toString();
        releaseDate = currentMovie.getRelease_date();
        overView = currentMovie.getOverview();
        posterPath = currentMovie.getPoster_path();

        original_title.setText(movieName);
        user_rating.setText(userRating);
        release_date.setText(releaseDate);
        overview.setText(overView);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        userReviewRecyclerView.setLayoutManager(layoutManager);
        trailerRecyclerView.setLayoutManager(layoutManager1);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        userReviewRecyclerView.setHasFixedSize(true);
        trailerRecyclerView.setHasFixedSize(true);


        Picasso.with(this).load("http://image.tmdb.org/t/p/w185/" + posterPath).into(poster_path);

        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            getLoaderManager().initLoader(0, null, this);
        }

        FavoritesDbHelper dbHelper = new FavoritesDbHelper(this);

        Cursor cursor = getContentResolver().query(CONTENT_URI,null,"movieId=?",new String[]{String.valueOf(id)},null);
        if (cursor.getCount() > 0){
            favoriteCheckBox.setChecked(true);
        }
        cursor.close();
        /*while (cursor.moveToNext()) {
            if (id == cursor.getInt(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID))) {
                favoriteCheckBox.setChecked(true);
            }
        }*/

        favoriteCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    addNewFavoriteMovie();
                } else {
                    removeFavoriteMovie();
                }
            }
        });

    }

    @Override
    public Loader<MovieDetails> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder videoUriBuilder = baseUri.buildUpon();
        Uri.Builder reviewUriBuilder = baseUri.buildUpon();

        reviewUriBuilder.appendPath(String.valueOf(id)).appendPath(REVIEWS_ENDPOINT).appendQueryParameter("api_key",API_KEY);
        videoUriBuilder.appendPath(String.valueOf(id)).appendPath(VIDEO_ENDPOINT).appendQueryParameter("api_key",API_KEY);

        return new MovieDetailsLoader(this,videoUriBuilder.toString(),reviewUriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<MovieDetails> loader, MovieDetails movieDetails) {
        ArrayList<String> youtubeKey = movieDetails.getYoutubeKeys();
        trailerRecyclerView.setAdapter(new TrailerAdapter(youtubeKey));

        ArrayList<UserReviews> userReviewsArrayList = movieDetails.getUserReviews();
        userReviewRecyclerView.setAdapter(new UserReviewsAdapter(userReviewsArrayList));
    }

    @Override
    public void onLoaderReset(Loader<MovieDetails> loader) {

    }

    private void addNewFavoriteMovie(){
        ContentValues cv = new ContentValues();

        cv.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_NAME, movieName);
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, id);
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW, overView );
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH, posterPath);
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE, releaseDate);
        cv.put(FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE, userRating);

        Uri uri = getContentResolver().insert(CONTENT_URI, cv);

    }

    private void removeFavoriteMovie(){
        getContentResolver().delete(CONTENT_URI, FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=" + id,null);
    }
}
