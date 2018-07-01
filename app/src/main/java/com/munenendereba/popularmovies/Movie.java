package com.munenendereba.popularmovies;

public class Movie {
    //use @SerializedName for those fields which are differently named than the name in json
    //@SerializedName("id")
    private String movieId; //id in API
    //@SerializedName("title")
    private String movieName; //title in API
    //@SerializedName("original_title")
    private String originalTitle;
    //@SerializedName("overview")
    private String plotSynopsis;//overview in API
    //@SerializedName("vote_average")
    private String userRating;//vote_average in API
    //@SerializedName("release_date")
    private String releaseDate;
    //@SerializedName("poster_path")
    private String moviePosterImage;
    private int movieIndex;

    //constructor
    public Movie(String movieId, int movieIndex, String title, String originalTitle, String poster, String userRating, String releaseDate, String plotSynopsis) {
        this.movieId = movieId;
        this.movieIndex = movieIndex;
        this.movieName = title;
        this.originalTitle = originalTitle;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.plotSynopsis = plotSynopsis;
        this.moviePosterImage = poster;
    }

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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePosterImage() {
        return moviePosterImage;
    }

    public void setMoviePosterImage(String moviePosterImage) {
        this.moviePosterImage = moviePosterImage;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }


    public int getMovieIndex() {
        return movieIndex;
    }

    public void setMovieIndex(int movieIndex) {
        this.movieIndex = movieIndex;
    }
}
