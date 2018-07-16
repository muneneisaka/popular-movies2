package com.munenendereba.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FavoriteMovieViewModel extends AndroidViewModel {
    private FavoriteMovieRepository favoriteMovieRepository;
    private LiveData<List<FavoriteMovie>> mAllFavoriteMovies;
    //private List<FavoriteMovie> mFavoriteMoviesInitial;

    public FavoriteMovieViewModel(Application application) {
        super(application);
        favoriteMovieRepository = new FavoriteMovieRepository(application);
        mAllFavoriteMovies = favoriteMovieRepository.getAllFavoriteMovies();
        // mFavoriteMoviesInitial = favoriteMovieRepository.getAllFavoriteMoviesInitial();
    }

    LiveData<List<FavoriteMovie>> getAllFavoriteMovies() {
        return mAllFavoriteMovies;
    }

   /* List<FavoriteMovie> getAllFavoriteMoviesInitial() {
        return favoriteMovieRepository.getAllFavoriteMoviesInitial();
    }*/

    public void insertFavoriteMovie(FavoriteMovie favoriteMovie) {
        favoriteMovieRepository.insertFavoriteMovie(favoriteMovie);
    }

    //accepts movie id as String, returns Favorite Movie
    public AsyncTask<String, Void, FavoriteMovie> getFavoriteMovie(String movieId) {
        return favoriteMovieRepository.getFavoriteMovie(movieId);
    }

    //accepts movie id as String, and returns an integer which is the number of rows deleted from table
    public AsyncTask<String, Void, Integer> removeFavoriteMovie(String movieId) {
        return favoriteMovieRepository.removeFavoriteMovie(movieId);
    }
}
