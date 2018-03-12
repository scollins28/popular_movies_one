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

    //Constructor that takes in and stores mURL.
    public FilmLoader (Context context, String url){
        super(context);
        mURL=url;}

    //Forces the load.
    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    //Loads the data for the grid in the background. If the url is null, terminates here. Initiates the Film Data fetchfilms methods (which then uses subsequent FilmData methods).
    @Override
    public List<Film> loadInBackground (){
        if (mURL == null){
            return null;
        }
        Log.e( LOG_TAG, mURL );
        List <Film> films = FilmData.fetchFilms( mURL );
         return films;
    }

}