package com.example.android.popularmovies.FavouritesDatabase;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.UriMatcher.NO_MATCH;
import static android.provider.BaseColumns._ID;
import static com.example.android.popularmovies.FavouritesDatabase.FavouritesContract.AUTHORITY;
import static com.example.android.popularmovies.FavouritesDatabase.FavouritesContract.FavouritesTable.TABLE_NAME;
import static com.example.android.popularmovies.FavouritesDatabase.FavouritesContract.PATH_FAVOURITES;

public class FavouritesContentProvider extends ContentProvider {

    private FavouritesDBHelper favDbHelper;
    public static final int FAVOURITES = 100;
    public static final int FAVOURITES_WITH_ID = 101;
    public static final int FAVOURITES_WITH_MOVIE_ID = 102;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher( NO_MATCH );
        uriMatcher.addURI( AUTHORITY, FavouritesContract.PATH_FAVOURITES, FAVOURITES );
        uriMatcher.addURI( AUTHORITY, FavouritesContract.PATH_FAVOURITES + "/#", FAVOURITES_WITH_ID );
        uriMatcher.addURI( AUTHORITY, FavouritesContract.PATH_FAVOURITES + "/*", FAVOURITES_WITH_MOVIE_ID);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        favDbHelper = new FavouritesDBHelper( context );
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String [] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = favDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor cursor;

        switch (match) {
            case FAVOURITES:
                cursor =  db.query(TABLE_NAME, null, null, null, null, null, null);
                break;
            case FAVOURITES_WITH_ID:
                String id = uri.getPathSegments().get( 1 );
                String mSelection = "_id=?";
                String [] mSelectionArgs = new String[]{id};
                cursor = db.query( TABLE_NAME, projection, mSelection, mSelectionArgs , null, null, sortOrder );
                break;
            case FAVOURITES_WITH_MOVIE_ID:
                String altID = uri.getPathSegments().get( 1 );
                String mAltSelection = "COLUMN_FILM_ID=?";
                String [] mAltSelectionArgs = new String[]{altID};
                cursor = db.query( TABLE_NAME, projection, mAltSelection, mAltSelectionArgs , null, null, sortOrder );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = favDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case FAVOURITES:
                long id = db.insert(TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(FavouritesContract.FavouritesTable.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = favDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int favsDeleted;

        switch (match) {
            case FAVOURITES_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String newSelection = "_ID=?";
                String [] newSelectionArgs = new String[]{id};
                favsDeleted = db.delete( TABLE_NAME, newSelection, newSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return favsDeleted;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
