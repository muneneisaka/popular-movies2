package com.munenendereba.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
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
    private String TMDBApiKey = BuildConfig.PopularMoviesTMDBAPIKey;
    MovieRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    String basePath = "https://api.themoviedb.org/3/movie/popular?api_key=" + TMDBApiKey;
    private String movieId;
    private int movieIndex;
    private String movieName;
    private String originalTitle;
    private String plotSynopsis;
    private String userRating;
    private String releaseDate;
    private String moviePosterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "reached onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //NOT LOGGING - TO REMOVE THE BELOW FROM MAIN THREAD
        //TO FIX THIS https://stackoverflow.com/questions/19266553/android-caused-by-android-os-networkonmainthreadexception
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        //TO REMOVE THE ABOVE


        //check for the orientation of the screen i.e. portrait and landscape and show the appropriate layout i.e. 2 or 4 columns
        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) numberOfColumns = 2;
        else if (orientation == Configuration.ORIENTATION_LANDSCAPE) numberOfColumns = 4;

        recyclerView = findViewById(R.id.rvNumbers);
        fetchMoviesData(basePath);
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
    }

    //method to inflate menu options
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

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
        /*Toast.makeText(this, "SEMA sema", Toast.LENGTH_SHORT).show();
        Log.i("TAG", "You clicked number " + adapter.getITem(position) + ", which is at cell position " + position);
        //ArrayList<String> moviePosters = jsonParser.returnMoviePosterList(basePath);
        //Log.i("FMUNENE", "semeni" + moviePosters.toString());

        ArrayList<String> moviePosters2 = jsonParser.returnMoviePosters(getResources().getString(R.string.movie_list_output));
        Log.i("YA PILI MUNENE", "semeni" + moviePosters2.toString());*/

        //fetchMoviesData();

        //launchDetailActivity(String.valueOf(position));
        launchDetailActivity(position);
    }

    private void launchDetailActivity(int index) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        //intent.putExtra("movieName", movieId);
        Movie movie = movies.get(index);

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
        // Movie movie = new Movie();
        RequestQueue queue = Volley.newRequestQueue(this);
        //Log.d("MUNENE-TEST-hapa", "Response Munene");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, path, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("MUNENE-TEST", "Response Munene" + response);
//                GsonBuilder builder = new GsonBuilder();
//                Gson mGson = builder.create();


                processJSon(response);
                /*List<ItemObject> posts = new ArrayList<ItemObject>();
                posts = Arrays.asList(mGson.fromJson(response, ItemObject[].class));
                adapter = new RecyclerViewAdapter(MainActivity.this, posts);
                recyclerView.setAdapter(adapter);*/
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
        //String r = s.toString();
        //Log.d("MUNENE-OBJECT-BEFORE", r);

        movies = new ArrayList<>();
        JSONObject jsonObject;
        ArrayList<String> res = new ArrayList<>();
        try {
            jsonObject = new JSONObject(r);

            JSONArray jr = jsonObject.getJSONArray("results");
            //Log.d("RESULTSI-LEBU", jr.toString());
            //[] - array; {} - object
            String imagePath;

            for (int i = 0; i < jr.length(); i++) {
                //Log.d("OBJECT-POSITION:" + i, jr.getJSONObject(i).getString("poster_path").toString() + "\n");
                //RETURN LIST OF THE IMAGE ARRAY
                //check for null images
                // if (jr.getJSONObject(i).getString("poster_path").toString() != "null") {
                //sample poster image http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg - w185 is size
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
                //Movie movie = new Movie(movieId, movieName, originalTitle, moviePosterImage, userRating, releaseDate, plotSynopsis);
                //movie.a
                // }
            }
            //recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
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
