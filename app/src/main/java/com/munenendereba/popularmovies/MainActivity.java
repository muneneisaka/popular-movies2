package com.munenendereba.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    JsonParser jsonParser = new JsonParser();
    String basePath = "https://api.themoviedb.org/3/movie/popular?api_key=13b8ef469540ea26085b5f22321b53dd";
    MovieRecyclerViewAdapter adapter;
    RecyclerView recyclerView;
    private ArrayList<String> imageList = new ArrayList<>();

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
        fetchMoviesData();
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));

        //adapter = new MovieRecyclerViewAdapter(this, data);
        //adapter.setClickListener(this);
        //recyclerView.setAdapter(adapter);
        //ImageView movie_image=(ImageView) findViewById(R.id.movie_image);


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

        launchDetailActivity(String.valueOf(position));
    }

    private void launchDetailActivity(String position) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("movieName", position);
        startActivity(intent);
    }

    private void fetchMoviesData() {
        Movie movie = new Movie();
        ArrayList<String> posters = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        //Log.d("MUNENE-TEST-hapa", "Response Munene");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, basePath, new Response.Listener<String>() {

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
        JSONObject jsonObject;
        ArrayList<String> res = new ArrayList<>();
        try {
            jsonObject = new JSONObject(r);

            JSONArray jr = jsonObject.getJSONArray("results");
            //Log.d("RESULTSI-LEBU", jr.toString());
            //[] - array; {} - object
            String imagePath = "";

            for (int i = 0; i < jr.length(); i++) {
                //Log.d("OBJECT-POSITION:" + i, jr.getJSONObject(i).getString("poster_path").toString() + "\n");
                //RETURN LIST OF THE IMAGE ARRAY
                //sample poster image http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg - w185 is size
                imagePath = "http://image.tmdb.org/t/p/w185" + jr.getJSONObject(i).getString("poster_path").toString();
                res.add(imagePath);
                //Log.d("OBJECT-POSITION:" + i, imagePath + "\n"); //Works produces e.g. D/OBJECT-POSITION:19: http://image.tmdb.org/t/p/w185/MvYpKlpFukTivnlBhizGbkAe3v.jpg
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
