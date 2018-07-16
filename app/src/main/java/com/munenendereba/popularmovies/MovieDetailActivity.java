package com.munenendereba.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MovieDetailActivity extends AppCompatActivity implements TrailersRecyclerViewAdapter.ItemClickListener {

    ReviewsRecyclerViewAdapter reviewsRecyclerViewAdapter;
    TrailersRecyclerViewAdapter trailersRecyclerViewAdapter;
    RecyclerView reviewRecyclerView;
    RecyclerView trailersRecyclerView;
    ArrayList<String> trailerVideos = new ArrayList<>();
    CompoundButton buttonFavorite;
    MainActivity mainActivity = new MainActivity();
    private String TMDBApiKey = BuildConfig.PopularMoviesTMDBAPIKey; //get the API Key from the gradle.properties file
    private FavoriteMovieViewModel favoriteMovieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();

        favoriteMovieViewModel = new FavoriteMovieViewModel(super.getApplication());

        reviewRecyclerView = findViewById(R.id.rvMovieReviews);
        trailersRecyclerView = findViewById(R.id.rvTrailerViews);

        final String movieId = intent.getStringExtra("movieId");

        fetchTrailers(movieId);//get the trailers
        trailersRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        fetchReviews(movieId);//ensure to get the data before setting layout manager
        reviewRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        //initialize the controls that we will load with the movie details
        TextView textOriginalTitle = findViewById(R.id.tv_original_title);
        TextView textUserRating = findViewById(R.id.tv_user_rating);
        TextView textYearReleased = findViewById(R.id.tv_release_date);
        TextView textPlotSynopsis = findViewById(R.id.tv_plot_synopsis);
        ImageView imageView = findViewById(R.id.imageView);

        final String userRating = intent.getStringExtra("userRating") + "/10";
        final String originalTitle = intent.getStringExtra("originalTitle");
        final String plotSynopsis = intent.getStringExtra("plotSynopsis");
        String releaseDate = intent.getStringExtra("releaseDate");

        //set the data to the controls
        textUserRating.setText(userRating);
        textOriginalTitle.setText(originalTitle);
        textPlotSynopsis.setText(plotSynopsis);

        //try catch to ensure that the date is correct format
        try {
            releaseDate = releaseDate.substring(0, 4);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        final String relDate = releaseDate;
        textYearReleased.setText(releaseDate);
        final String posterPath = intent.getStringExtra("moviePoster");

        //use Picasso to show the movie poster
        Picasso.get()
                .load(posterPath)
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(imageView);

        buttonFavorite = findViewById(R.id.buttonFavorite);
        //if a movie is in the favorites database, then show the heart as marked i.e. the movie is already favorite
        //MUST be before setOnCheckedChangeListener otherwise any time open a favorite movie, it shows added to favorites
        if (checkMovieIsFavorite(movieId)) buttonFavorite.setChecked(true);

        //set the favorites button to listen to event changes and process them
        buttonFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                processFavorites(isChecked, movieId, originalTitle, relDate, userRating, posterPath, plotSynopsis);
            }
        });


        setTitle(intent.getStringExtra("movieName"));
    }

    //this method checks if a movie is in the favorites, by querying the database comparing the movie id to saved movies in the db
    private boolean checkMovieIsFavorite(String movieId) {
        AsyncTask<String, Void, FavoriteMovie> favoriteMovie = favoriteMovieViewModel.getFavoriteMovie(movieId);
        FavoriteMovie f;
        try {
            f = favoriteMovie.get();//get the movie
            if (f != null) return true; //if the result is not null, then the movie is in the database
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    //this method accepts the movie id as input, and returns all the reviews for the movie and displays them in a recyclerview
    private void fetchReviews(String movieId) {
        String path = "https://api.themoviedb.org/3/movie/" + movieId + "/reviews?api_key=" + TMDBApiKey;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d("MUNENE-TEST", "Response Munene" + response);
                processReviewsJSon(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FETCH-REVIEW-ERROR", "Error " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    private ArrayList<String> processReviewsJSon(String r) {
        JSONObject jsonObject;
        ArrayList<String> res = new ArrayList<>();
        try {
            jsonObject = new JSONObject(r);

            JSONArray jr = jsonObject.getJSONArray("results");
            //[] - array; {} - object
            String review;

            //learnt how to process json from my sandwich club assignment
            //and here https://stackoverflow.com/questions/9605913/how-do-i-parse-json-in-android
            for (int i = 0; i < jr.length(); i++) {
                review = "Review by: " + jr.getJSONObject(i).getString("author").toString() + "\n" + jr.getJSONObject(i).getString("content").toString();
                res.add(review);

                //Log.d("GET-REVIEW: " + i, review);
            }

            //Log.d("ALL-DATA:", res.toString());
            reviewsRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(this, res);
            reviewRecyclerView.setAdapter(reviewsRecyclerViewAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //check to see if there are any reviews and show appropriate message
        if (res.isEmpty()) res.add("This movie has no reviews yet");
        return res;
    }

    private void fetchTrailers(String movieId) {
        String path = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + TMDBApiKey;
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d("MUNENE-TEST", "Response Munene" + response);
                processTrailersJSon(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FETCH-TRAILER-ERROR", "Error " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    private ArrayList<String> processTrailersJSon(String r) {
        JSONObject jsonObject;
        ArrayList<String> res = new ArrayList<>();
        try {
            jsonObject = new JSONObject(r);

            JSONArray jr = jsonObject.getJSONArray("results");
            //[] - array; {} - object
            String trailerImage;

            for (int i = 0; i < jr.length(); i++) {
                //trailer = "https://www.youtube.com/watch?v=" + jr.getJSONObject(i).getString("key").toString();
                String video = jr.getJSONObject(i).getString("key").toString();
                //show trailer images from thumbnails like https://img.youtube.com/vi/v7MGUNV8MxU/0.jpg
                trailerImage = "https://img.youtube.com/vi/" + video + "/0.jpg"; //trailer image
                res.add(trailerImage); //add actual trailer video to play to array list
                trailerVideos.add(video);
                //Log.d("GET-TRAILER-IMAGE: " + i, trailerImage);
            }

            //Log.d("ALL-DATA:", res.toString());
            trailersRecyclerViewAdapter = new TrailersRecyclerViewAdapter(this, res);
            trailersRecyclerViewAdapter.setClickListener(this);
            trailersRecyclerView.setAdapter(trailersRecyclerViewAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //check to see if there are any reviews and show appropriate message
        //if (res.isEmpty()) res.add("This movie has no reviews yet");
        return res;
    }

    //launch trailers if there are some
    @Override
    public void onItemClick(View view, int position) {
        //Log.d("ATI NINI", "WHy is it not logging: " + position);
        String vid = "";
        if (!trailerVideos.isEmpty()) vid = trailerVideos.get(position);
        launchTrailerIntent(vid);
    }

    private void launchTrailerIntent(String trailerVideoToPlay) {
        Uri videoUri = Uri.parse("vnd.youtube://" + trailerVideoToPlay);
        Intent intent = new Intent(Intent.ACTION_VIEW, videoUri);
        //check if there's an app available to fulfill our request
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    //add movies to the favorites when user likes them, remove when unchecked
    private void processFavorites(Boolean isChecked, String movieId, String originalTitle, String releaseDate, String voterRating, String posterPath, String plotSynopsis) {
        FavoriteMovie favoriteMovie = new FavoriteMovie(movieId, originalTitle, releaseDate, voterRating, posterPath, plotSynopsis);
        if (isChecked) {
            //add a movie to the database
            try {
                favoriteMovieViewModel.insertFavoriteMovie(favoriteMovie);
                Toast.makeText(this, "The movie has been added to your favorites", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //TODO: if the last movie is deleted, show a message and move back to popular movies or in the recylerview, show a placeholder image
            //delete movie from favorites
            try {
                AsyncTask<String, Void, Integer> l = favoriteMovieViewModel.removeFavoriteMovie(movieId);
                if (l.get() > 0) {
                    Toast.makeText(this, "The movie has been removed from your favorites", Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        //mainActivity.showFavoriteMovies(favoriteMovieViewModel.getAllFavoriteMovies().getValue());

        /*//whenever we add or delete a movie, observe the changes and update the recycler view
        favoriteMovieViewModel.getAllFavoriteMovies().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovie> favoriteMovies) {
                ArrayList<String> res = mainActivity.showFavoriteMovies(favoriteMovies);
                *//*MovieRecyclerViewAdapter mainMovieAdapter = new MovieRecyclerViewAdapter(this, res);
                mainMovieAdapter.setClickListener(this);
                mainMovieRecyclerView.setAdapter(mainMovieAdapter);*//*
                setTitle("My Favorite Movies");
                //if the favorite movie list is not empty, then show the list, otherwise show a message
                //if (favoriteMovies.size() == 0)

            }
        });*/
    }


}