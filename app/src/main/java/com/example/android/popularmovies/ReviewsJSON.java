package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by scoll on 27/02/2018.
 */

public class ReviewsJSON {

    //Private Strings of all movie variables
    private String mAuthor;
    private String mReview;

    //Basic constructor requiring all the variables of the movie.
    ReviewsJSON (String author, String review){
            mAuthor = author;
            mReview = review;

    }

    public String getAuthor (){
        return mAuthor;
    }
    public String getReview (){
        return mReview;
    }

}
