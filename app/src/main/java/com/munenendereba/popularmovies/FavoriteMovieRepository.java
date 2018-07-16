package com.munenendereba.popularmovies;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FavoriteMovieRepository {
    private FavoriteMovieDao mFavoriteMovieDao;
    private LiveData<List<FavoriteMovie>> mAllFavoriteMovies;

    FavoriteMovieRepository(Application application) {
        FavoriteMovieRoomDatabase db = FavoriteMovieRoomDatabase.getDatabase(application);
        mFavoriteMovieDao = db.favoriteMovieDao();
        mAllFavoriteMovies = mFavoriteMovieDao.getAllFavoriteMovies();
    }

    //returns all movies with LiveData observer methods
    LiveData<List<FavoriteMovie>> getAllFavoriteMovies() {
        return mAllFavoriteMovies;
    }

    //insert movie details to the favorites table asynchronously
    public void insertFavoriteMovie(FavoriteMovie favoriteMovie) {
        new insertAsyncTask(mFavoriteMovieDao).execute(favoriteMovie);
    }

    //get one movie by async takes a String parameter, and returns a movie object
    public AsyncTask<String, Void, FavoriteMovie> getFavoriteMovie(String movieId) {
        return new getAsyncTask(mFavoriteMovieDao).execute(movieId);
    }

    //Async method to remove a movie from the db
    public AsyncTask<String, Void, Integer> removeFavoriteMovie(String movieId) {
        return new deleteAsyncTask(mFavoriteMovieDao).execute(movieId);
    }

    private static class insertAsyncTask extends AsyncTask<FavoriteMovie, Void, Void> {

        private FavoriteMovieDao mAsyncTaskDao;

        insertAsyncTask(FavoriteMovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavoriteMovie... params) {
            mAsyncTaskDao.insertFavoriteMovie(params[0]);
            return null;
        }
    }

    //inner class implementation of the async method
    private static class getAsyncTask extends AsyncTask<String, Void, FavoriteMovie> {

        private FavoriteMovieDao mAsyncTaskDao;

        getAsyncTask(FavoriteMovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected FavoriteMovie doInBackground(final String... params) {
            return mAsyncTaskDao.getFavoriteMovie(params[0]);
        }
    }

    //static inner class to delete the movie
    private static class deleteAsyncTask extends AsyncTask<String, Void, Integer> {

        private FavoriteMovieDao mAsyncTaskDao;

        deleteAsyncTask(FavoriteMovieDao dao) {
            mAsyncTaskDao = dao;
        }

        //(String... params) accepts an infinite number of parameters
        @Override
        protected Integer doInBackground(final String... params) {
            return mAsyncTaskDao.deleteFavoriteMovie(params[0]);
        }
    }
}
