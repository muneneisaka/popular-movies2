package com.munenendereba.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();

        //initialize the controls that we will load with the movie details
        TextView textOriginalTitle = findViewById(R.id.tv_original_title);
        TextView textUserRating = findViewById(R.id.tv_user_rating);
        TextView textYearReleased = findViewById(R.id.tv_release_date);
        TextView textPlotSynopsis = findViewById(R.id.tv_plot_synopsis);
        ImageView imageView = findViewById(R.id.imageView);

        //set the data to the controls
        textUserRating.setText(intent.getStringExtra("userRating") + "/10");
        textOriginalTitle.setText(intent.getStringExtra("originalTitle"));
        textPlotSynopsis.setText(intent.getStringExtra("plotSynopsis"));
        String releaseDate = intent.getStringExtra("releaseDate");

        //try catch to ensure that the date is correct format
        try {
            releaseDate = releaseDate.substring(0, 4);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        textYearReleased.setText(releaseDate);

        //use Picasso to show the movie poster
        Picasso.get()
                .load(intent.getStringExtra("moviePoster"))
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .into(imageView);

        setTitle(intent.getStringExtra("movieName"));
    }
}