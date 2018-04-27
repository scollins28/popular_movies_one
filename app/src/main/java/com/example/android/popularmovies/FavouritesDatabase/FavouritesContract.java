package com.example.android.popularmovies.FavouritesDatabase;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavouritesContract {

    //To prevent accidental calling, constructor is private
    private FavouritesContract(){};

    public static final String AUTHORITY = "com.example.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse( "content://"+AUTHORITY );
    public static final String PATH_FAVOURITES = "Favourites";

    public static class FavouritesTable implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath( PATH_FAVOURITES ).build();

        public static final String TABLE_NAME = "Favourites";
        public static final String COLUMN_FILM_NAME = "Name";
        public static final String COLUMN_RATING = "Rating";
        public static final String COLUMN_DATE_RELEASED = "Date";
        public static final String COLUMN_DESC = "Description";
        public static final String COLUMN_YOUTUBE_ONE = "YouTubeOne";
        public static final String COLUMN_YOUTUBE_TWO = "YouTubeTwo";
        public static final String COLUMN_AUTHOR_ONE = "AuthorOne";
        public static final String COLUMN_REVIEW_ONE = "ReviewOne";
        public static final String COLUMN_AUTHOR_TWO = "AuthorTwo";
        public static final String COLUMN_REVIEW_TWO = "ReviewTwo";
        public static final String COLUMN_FAVOURITED = "Favourited";
        public static final String COLUMN_IMAGE = "Image";
        public static final String COLUMN_FILM_ID = "FilmID";

        public static Uri deleteFavouriteUri(long _ID) {
            return CONTENT_URI.buildUpon()
                    .appendPath(Long.toString(_ID))
                    .build();
        }
    }
}