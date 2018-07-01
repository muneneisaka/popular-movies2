package com.munenendereba.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements MovieRecyclerViewAdapter.ItemClickListener {

    private static final String TAG = "MainActivityLogger";
    int numberOfColumns = 2;//default number of movie list
    ArrayList<Movie> movies;
    private String TMDBApiKey = BuildConfig.PopularMoviesTMDBAPIKey; //get the API Key from the gradle.properties fiel
    MovieRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    String basePath = "https://api.themoviedb.org/3/movie/popular?api_key=" + TMDBApiKey; //API request string
    private String movieId;
    private int movieIndex;
    private String movieName;
    private String originalTitle;
    private String plotSynopsis;
    private String userRating;
    private String releaseDate;
    private String moviePosterImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check for the orientation of the screen i.e. portrait and landscape and show the appropriate layout i.e. 2 or 4 columns
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) numberOfColumns = 2;
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) numberOfColumns = 4;

        //if there's internet connection show the main layout, otherwise show the internet error layout
        if (checkInternetConnection()) {
            //get the recyclerView to display image
            recyclerView = findViewById(R.id.rvImageViews);
            fetchMoviesData(basePath);
            recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        } else {
            Intent intent = new Intent(this, InternetError.class);
            startActivity(intent);
            this.finish(); //finish this activity to prevent navigating back to it
        }
    }

    //method to check for internet connection and present appropriate UI
    public boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        //Log.d("INTERNET-CHECKER", "There's Internet Munene");
//Log.d("INTERNET-CHECKER", "Hakuna Internet Munene");
        return info != null && info.isConnectedOrConnecting();
    }

    //method to inflate menu options
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //check the menu item selected and launch the appropriate API call for sorting the movies
    public boolean onOptionsItemSelected(MenuItem item) {
        String path = "https://api.themoviedb.org/3/discover/movie?api_key=" + TMDBApiKey + "&language=en-US&include_adult=false&include_video=false&page=1";
        if (item.getItemId() == R.id.sort_popular) {
            fetchMoviesData(path + "&sort_by=popularity.desc");
            return true;
        } else if (item.getItemId() == R.id.sort_top_rated) {
            fetchMoviesData(path + "&sort_by=vote_average.desc");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        //show details of the movie
        launchDetailActivity(position);
    }

    private void launchDetailActivity(int index) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        Movie movie = movies.get(index);

        //send the movie data for the selected movie poster to the MovieDetailActivity
        intent.putExtra("movieName", movie.getMovieName());
        intent.putExtra("movieId", movie.getMovieId());
        intent.putExtra("userRating", movie.getUserRating());
        intent.putExtra("originalTitle", movie.getOriginalTitle());
        intent.putExtra("plotSynopsis", movie.getPlotSynopsis());
        intent.putExtra("releaseDate", movie.getReleaseDate());
        intent.putExtra("moviePoster", movie.getMoviePosterImage());

        startActivity(intent);
    }

    private void fetchMoviesData(String path) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Log.d("MUNENE-TEST", "Response Munene" + response);
                processJSon(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error " + error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    private ArrayList<String> processJSon(String r) {
        movies = new ArrayList<>();
        JSONObject jsonObject;
        ArrayList<String> res = new ArrayList<>();
        try {
            jsonObject = new JSONObject(r);

            JSONArray jr = jsonObject.getJSONArray("results");
            //[] - array; {} - object
            String imagePath;

            for (int i = 0; i < jr.length(); i++) {
                imagePath = "http://image.tmdb.org/t/p/w185" + jr.getJSONObject(i).getString("poster_path").toString();
                res.add(imagePath);
                movieIndex = i;
                movieName = jr.getJSONObject(i).getString("title").toString();
                movieId = jr.getJSONObject(i).getString("id").toString();
                moviePosterImage = imagePath;
                originalTitle = jr.getJSONObject(i).getString("original_title").toString();
                plotSynopsis = jr.getJSONObject(i).getString("overview").toString();
                releaseDate = jr.getJSONObject(i).getString("release_date").toString();
                userRating = jr.getJSONObject(i).getString("vote_average").toString();

                movies.add(new Movie(movieId, movieIndex, movieName, originalTitle, moviePosterImage, userRating, releaseDate, plotSynopsis));
                //Log.d("Check-Object-Munene", movies.get(i).getMoviePosterImage());
            }

            //Log.d("ALL-DATA:", res.toString());
            adapter = new MovieRecyclerViewAdapter(this, res);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }
}
