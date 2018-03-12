package com.example.android.popularmovies;

/**
 * Created by scoll on 27/02/2018.
 */

public class Film {

    //Private Strings of all movie variables
    private String mMovieName;
    private String mMovieReleaseDate;
    private int mMovieRating;
    private String mMovieSummary;
    private String mFilmImageUrl;

    //Basic constructor requiring all the variables of the movie.
    Film (String movieName, String movieReleaseDate, int movieRating, String movieSummary, String filmUrl){
        mMovieName = movieName;
        mMovieReleaseDate = movieReleaseDate;
        mMovieRating = movieRating;
        mMovieSummary = movieSummary;
        mFilmImageUrl = filmUrl;
    }

    //Function to retrieve the movie name
    public String getMovieName (){
        return mMovieName;
    }

    //Function to retrieve the movie release date
    public String getmMovieReleaseDate (){
        return mMovieReleaseDate;
    }

    //Function to retrieve the movie rating
    public int getMovieRating (){
        return mMovieRating;
    }

    //Function to retrieve the movie summary
    public String getMovieSummary (){
        return mMovieSummary;
    }

    //Function to retrieve the movie summary
    public String getMovieImageUrl (){
        return mFilmImageUrl;
    }

}
