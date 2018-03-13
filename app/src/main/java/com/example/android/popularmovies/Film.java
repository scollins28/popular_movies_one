package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by scoll on 27/02/2018.
 */

public class Film implements Parcelable {

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

    protected Film(Parcel in) {
        mMovieName = in.readString();
        mMovieReleaseDate = in.readString();
        mMovieRating = in.readInt();
        mMovieSummary = in.readString();
        mFilmImageUrl = in.readString();
    }

    public static final Creator<Film> CREATOR = new Creator<Film>() {
        @Override
        public Film createFromParcel(Parcel in) {
            return new Film( in );
        }

        @Override
        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    //Function to retrieve the movie name
    public String getMovieName (){
        return mMovieName;
    }

    //Function to retrieve the movie release date
    public String getmMovieReleaseDate (){
        return mMovieReleaseDate;
    }

    //Function to retrieve the movie rating
    public int getMovieRating (){return mMovieRating;}

    //Function to retrieve the movie summary
    public String getMovieSummary (){
        return mMovieSummary;
    }

    //Function to retrieve the movie summary
    public String getMovieImageUrl (){
        return mFilmImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( mMovieName );
        dest.writeString( mMovieReleaseDate );
        dest.writeInt( mMovieRating );
        dest.writeString( mMovieSummary );
        dest.writeString( mFilmImageUrl );
    }
}
