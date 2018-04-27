package com.example.android.popularmovies;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by scoll on 01/03/2018.
 */

public class FilmLoader extends AsyncTaskLoader<List<Film>> {
    private static final String LOG_TAG = FilmLoader.class.getSimpleName();
    private String mURL;
    private int webOrLocal = 0;

    //Constructor that takes in and stores mURL.
    public FilmLoader (Context context, String url){
        super(context);
        mURL=url;
        webOrLocal = 1;}

    public FilmLoader (Context context){
        super(context);
    }

    //Forces the load.
    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    //Loads the data for the grid in the background. If the url is null, terminates here. Initiates the Film Data fetchfilms methods (which then uses subsequent FilmData methods).
    @Override
    public List<Film> loadInBackground (){
        List<Film> films;
        if (mURL == null && webOrLocal ==1){
            return null;
        }
        else if (mURL!= null && webOrLocal == 1) {
            Log.e( LOG_TAG, mURL );
            films = FilmData.fetchFilms( mURL );
        }
        else {
            films = FilmData.extractFeaturesFromFavouriteFilmsCursor( MainActivity.cursor );
        }
         return films;
    }

}