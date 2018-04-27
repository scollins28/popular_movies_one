package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by scoll on 27/02/2018.
 */

public class YoutubeJSON implements Parcelable {

    //Private Strings of all movie variables
    private String mYoutubeKey;

    //Basic constructor requiring all the variables of the movie.
    YoutubeJSON(String key){
        mYoutubeKey = key;
    }

    protected YoutubeJSON(Parcel in) {
        mYoutubeKey = in.readString();
    }

    public static final Creator<YoutubeJSON> CREATOR = new Creator<YoutubeJSON>() {
        @Override
        public YoutubeJSON createFromParcel(Parcel in) {
            return new YoutubeJSON( in );
        }

        @Override
        public YoutubeJSON[] newArray(int size) {
            return new YoutubeJSON[size];
        }
    };

    //Function to retrieve the movie name
    public String getYoutubeKey (){
        return mYoutubeKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString (mYoutubeKey);
    }
}
