package com.munenendereba.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class InternetError extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Popular Movies - Sorry, no connection");
        setContentView(R.layout.activity_internet_error);

    }

}
