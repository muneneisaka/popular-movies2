package com.munenendereba.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

//interface for the database access object
@Dao
public interface FavoriteMovieDao {

    //inserts a single movie to the favorites table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFavoriteMovie(FavoriteMovie favoriteMovie);

    //deletes all movies from the favorites table
    @Query("DELETE FROM favorite_movie")
    void deleteAllFavoriteMovies();

    //selects all movies from the favorites table
    @Query("SELECT * from favorite_movie")
    LiveData<List<FavoriteMovie>> getAllFavoriteMovies();

    //deletes a movie given a movie id
    @Query("DELETE FROM favorite_movie WHERE movie_id=:movie_id")
    int deleteFavoriteMovie(String movie_id);

    //selects a movie given a movie id
    @Query("SELECT * from favorite_movie WHERE movie_id=:movie_id")
    FavoriteMovie getFavoriteMovie(String movie_id);
}
