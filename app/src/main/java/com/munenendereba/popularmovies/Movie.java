package com.munenendereba.popularmovies;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Movie {
    //use @SerializedName for those fields which are differently named than the name in json
    @SerializedName("title")
    private String movieName;
    private String originalTitle;
    @SerializedName("overview")
    private String plotSynopsis;//overview in API
    @SerializedName("vote_average")
    private String userRating;//vote_average in API
    private Date releaseDate;
    private String moviePosterImage;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePosterImage() {
        return moviePosterImage;
    }

    public void setMoviePosterImage(String moviePosterImage) {
        this.moviePosterImage = moviePosterImage;
    }

}
