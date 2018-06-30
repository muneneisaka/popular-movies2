package com.munenendereba.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MovieDetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();
        String nameOfMovie = intent.getStringExtra("movieName");
        //Toast.makeText(this,nameOfMovie,Toast.LENGTH_SHORT).show();
        setTitle(nameOfMovie);
    }
}
