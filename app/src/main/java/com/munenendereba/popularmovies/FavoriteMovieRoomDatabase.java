package com.munenendereba.popularmovies;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {FavoriteMovie.class}, version = 1)
public abstract class FavoriteMovieRoomDatabase extends RoomDatabase {
    private static FavoriteMovieRoomDatabase INSTANCE;

    static FavoriteMovieRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavoriteMovieRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoriteMovieRoomDatabase.class, "favorite_movie_database")
                            //.addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract FavoriteMovieDao favoriteMovieDao();
/*
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final FavoriteMovieDao mDao;

        PopulateDbAsync(FavoriteMovieRoomDatabase db) {
            mDao = db.favoriteMovieDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            //mDao.deleteAllFavoriteMovies();
            FavoriteMovie favoriteMovie = new FavoriteMovie(params[0]);
            mDao.insertFavoriteMovie(favoriteMovie);
            return null;
        }
    }*/
}
