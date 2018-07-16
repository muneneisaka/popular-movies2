package com.munenendereba.popularmovies;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//Model class for favorite movies to be saved in the db
@Entity(tableName = "favorite_movie")
public class FavoriteMovie {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movie_id")
    private String movieId;
    @NonNull
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    @ColumnInfo(name = "release_date")
    private String releaseDate;
    @ColumnInfo(name = "voter_rating")
    private String voterRating;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    @ColumnInfo(name = "plot_synopsis")
    private String plotSynopsis;

    public FavoriteMovie(String movieId, String originalTitle, String releaseDate, String voterRating, String posterPath, String plotSynopsis) {
        this.movieId = movieId;
        this.originalTitle = originalTitle;
        this.releaseDate = releaseDate;
        this.voterRating = voterRating;
        this.posterPath = posterPath;
        this.plotSynopsis = plotSynopsis;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVoterRating() {
        return voterRating;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }
}
