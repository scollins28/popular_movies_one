package com.example.android.popularmovies.FavouritesDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.popularmovies.FavouritesDatabase.FavouritesContract;
import com.example.android.popularmovies.FavouritesDatabase.FavouritesContract.FavouritesTable;

public class FavouritesDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="favourites.db";
    private static final int DATABASE_VERSION = 1;

    public FavouritesDBHelper (Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " + FavouritesTable.TABLE_NAME +
                "(" + FavouritesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavouritesTable.COLUMN_FILM_NAME + " TEXT NOT NULL," +
                FavouritesTable.COLUMN_RATING + " INTEGER NOT NULL," +
                FavouritesTable.COLUMN_DATE_RELEASED + " TEXT NOT NULL," +
                FavouritesTable.COLUMN_DESC + " TEXT NOT NULL," +
                FavouritesTable.COLUMN_YOUTUBE_ONE + " TEXT NOT NULL, " +
                FavouritesTable.COLUMN_YOUTUBE_TWO + " TEXT NOT NULL, " +
                FavouritesTable.COLUMN_AUTHOR_ONE + " TEXT NOT NULL, " +
                FavouritesTable.COLUMN_REVIEW_ONE + " TEXT NOT NULL, " +
                FavouritesTable.COLUMN_AUTHOR_TWO + " TEXT NOT NULL, " +
                FavouritesTable.COLUMN_REVIEW_TWO + " TEXT NOT NULL, " +
                FavouritesTable.COLUMN_FAVOURITED + " INTEGER NOT NULL," +
                FavouritesTable.COLUMN_IMAGE + " TEXT NOT NULL," +
                FavouritesTable.COLUMN_FILM_ID + " TEXT NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouritesTable.TABLE_NAME);
        onCreate( db );
    }

}
