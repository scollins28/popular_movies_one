package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.popularmovies.FavouritesDatabase.FavouritesContract.FavouritesTable;
import com.example.android.popularmovies.FavouritesDatabase.FavouritesDBHelper;
import com.squareup.picasso.Picasso;
import java.util.List;
import static com.example.android.popularmovies.FavouritesDatabase.FavouritesContract.FavouritesTable.*;

/**
 * Created by scoll on 27/02/2018.
 */

public class DetailsActivity extends AppCompatActivity {
    private int detailsInt = 0;
    private int favourited = 0;
    private static final String LOG_TAG = "Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.details_activity );
        FavouritesDBHelper favDbHelper = new FavouritesDBHelper( this);

        //Pulls in the JSON information from the main activity.
        List<Film> imageAdapterfilms = MainActivity.sharingArrays();

        //Finds which film the information is related to.
        int filmNumber = getIntent().getIntExtra("Position", 0);
        final Film currentFilm = imageAdapterfilms.get(filmNumber);


        if (findFavouritesID( currentFilm.getMovieID())!=999999999){
            favourited=1;
        };

        if (favourited == 1){
            TextView favouritesText = (TextView) findViewById( R.id.favourites_text );
            ImageView heartButton = (ImageView) findViewById( R.id.favourite_button );
            ImageView unheartButton = (ImageView) findViewById( R.id.unfavourite_button );
            favouritesText.setText( R.string.unfavourite_text );
            heartButton.setVisibility( View.VISIBLE );
            unheartButton.setVisibility( View.GONE );

        }

        //Calls all of the film data methods to retrieve the information particular to this film.
        ImageView movieImage = (ImageView) findViewById( R.id.details_image );
        TextView movieTitle = (TextView) findViewById( R.id.details_title);
        TextView movieRelease = (TextView) findViewById( R.id.details_release_date);
        TextView movieSynopsis = (TextView) findViewById( R.id.details_synopsis);
        RatingBar movieRating = (RatingBar) findViewById( R.id.details_rating);
        LinearLayout reviewsSection = (LinearLayout) findViewById( R.id.reviews_section );
        LinearLayout favouritesSection = (LinearLayout) findViewById( R.id.favourites_section );
        final ImageButton youtubeOne = (ImageButton) findViewById( R.id.youtube_button_one );
        ImageButton youtubeTwo = (ImageButton) findViewById( R.id.youtube_button_two );
        TextView authorOne = (TextView) findViewById( R.id.reviews_author_one);
        TextView authorTwo = (TextView) findViewById( R.id.reviews_author_two);
        TextView reviewOne = (TextView) findViewById( R.id.reviews_review_one);
        TextView reviewTwo = (TextView) findViewById( R.id.reviews_review_two);




        //Uses the information retrieved above to set the data of the details activity.
        movieTitle.setText( currentFilm.getMovieName());
        movieRelease.setText( currentFilm.getmMovieReleaseDate());
        movieSynopsis.setText( currentFilm.getMovieSummary());
        movieRating.setNumStars( (int) currentFilm.getMovieRating() );
        authorOne.setText(currentFilm.getMovieAuthorOne() );
        reviewOne.setText( currentFilm.getReviewOne() );
        authorTwo.setText( currentFilm.getAuthorTwo() );
        reviewTwo.setText( currentFilm.getReviewTwo() );
        String moviePicassoImage = "http://image.tmdb.org/t/p/w500/".concat( currentFilm.getMovieImageUrl() );
        Picasso.with(this).load(moviePicassoImage).into(movieImage);

        setTitle(currentFilm.getMovieName());

        reviewsSection.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (detailsInt==0){
                    revealReviews();
                }
                else if (detailsInt==1){
                    hideReviews();
                }
            }
        } );

        favouritesSection.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView favouritesText = (TextView) findViewById( R.id.favourites_text );
                ImageView heartButton = (ImageView) findViewById( R.id.favourite_button );
                ImageView unheartButton = (ImageView) findViewById( R.id.unfavourite_button );
                if (favourited==0){
                    favouritesText.setText( R.string.unfavourite_text );
                    heartButton.setVisibility( View.VISIBLE );
                    unheartButton.setVisibility( View.GONE );
                    favourited=1;
                    getContentResolver().insert( CONTENT_URI, newContentValues( currentFilm ));
                }
                else if (favourited==1){
                    favouritesText.setText( R.string.favourite_text );
                    heartButton.setVisibility( View.GONE);
                    unheartButton.setVisibility( View.VISIBLE );
                    favourited=0;
                    Long currentMovieTableID = findFavouritesID( currentFilm.getMovieID() );
                    getContentResolver().delete( deleteFavouriteUri( currentMovieTableID ), null, null );
                }
                }
        } );

        youtubeOne.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (currentFilm.getMovieYoutubeOne()=="void"){
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText( getApplicationContext(), "Sorry the video doesn't exist yet, please try again later.", duration );
                toast.show();
            } else {
                String youtubeFull = MainActivity.getYoutubeBaseQuery().concat( currentFilm.getMovieYoutubeOne() );
                Intent intent = new Intent( Intent.ACTION_VIEW );
                intent.setData( Uri.parse( youtubeFull ) );
                if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity( intent );
            }}
            }
        } );

        youtubeTwo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFilm.getMovieYoutubeTwo() == "void") {
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText( getApplicationContext(), "Sorry the video doesn't exist yet, please try again later.", duration );
                    toast.show();
                } else {
                    String youtubeFull = MainActivity.getYoutubeBaseQuery().concat( currentFilm.getMovieYoutubeTwo() );
                    Intent intent = new Intent( Intent.ACTION_VIEW );
                    intent.setData( Uri.parse( youtubeFull ) );
                    if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity( intent );
                }}
            }
        } );
    }

    public static ContentValues newContentValues (Film currentFilm) {
        ContentValues contentValues = new ContentValues();
        contentValues.put( COLUMN_FILM_NAME, currentFilm.getMovieName() );
        contentValues.put( COLUMN_RATING, currentFilm.getMovieRating() );
        contentValues.put( COLUMN_DATE_RELEASED, currentFilm.getmMovieReleaseDate() );
        contentValues.put( COLUMN_DESC, currentFilm.getMovieSummary() );
        contentValues.put( COLUMN_YOUTUBE_ONE, currentFilm.getMovieYoutubeOne() );
        contentValues.put( COLUMN_YOUTUBE_TWO, currentFilm.getMovieYoutubeTwo() );
        contentValues.put( COLUMN_AUTHOR_ONE, currentFilm.getMovieAuthorOne() );
        contentValues.put( COLUMN_REVIEW_ONE, currentFilm.getReviewOne() );
        contentValues.put( COLUMN_AUTHOR_TWO, currentFilm.getAuthorTwo() );
        contentValues.put( COLUMN_REVIEW_TWO, currentFilm.getReviewTwo() );
        contentValues.put( COLUMN_FAVOURITED, 1 );
        contentValues.put( COLUMN_IMAGE, currentFilm.getMovieImageUrl() );
        contentValues.put( COLUMN_FILM_ID, currentFilm.getMovieID() );
    return contentValues;
    }

    public long findFavouritesID (String movieID){
        Cursor cursorTwo = getContentResolver().query(CONTENT_URI, null, null, null, null);
        cursorTwo.moveToFirst();
        for (int i=0; i<cursorTwo.getCount(); i++){
            cursorTwo.moveToPosition( i );
            String cursorMovieID = cursorTwo.getString( cursorTwo.getColumnIndex( FavouritesTable.COLUMN_FILM_ID ) );
            if (movieID.contentEquals( cursorMovieID )) {
                long idOfMatch = cursorTwo.getLong( cursorTwo.getColumnIndex( FavouritesTable._ID ) );
                Log.e( "ID", Long.toString(idOfMatch) );
                return idOfMatch;
            }
        }
        cursorTwo.close();
        return 999999999;
    }

    public void revealReviews (){
        TextView authorOne = (TextView) findViewById( R.id.reviews_author_one);
        TextView authorTwo = (TextView) findViewById( R.id.reviews_author_two);
        TextView reviewOne = (TextView) findViewById( R.id.reviews_review_one);
        TextView reviewTwo = (TextView) findViewById( R.id.reviews_review_two);
        ImageView dropDownArrow = (ImageView) findViewById( R.id.dropdown_arrow );
        ImageView dropDownArrowUp = (ImageView) findViewById( R.id.dropdown_arrow_up );
        authorOne.setVisibility( View.VISIBLE );
        authorTwo.setVisibility( View.VISIBLE );
        reviewOne.setVisibility( View.VISIBLE );
        reviewTwo.setVisibility( View.VISIBLE );
        dropDownArrow.setVisibility( View.GONE );
        dropDownArrowUp.setVisibility( View.VISIBLE );
        detailsInt=1;
    }

    public void hideReviews(){
        TextView authorOne = (TextView) findViewById( R.id.reviews_author_one);
        TextView authorTwo = (TextView) findViewById( R.id.reviews_author_two);
        TextView reviewOne = (TextView) findViewById( R.id.reviews_review_one);
        TextView reviewTwo = (TextView) findViewById( R.id.reviews_review_two);
        ImageView dropDownArrow = (ImageView) findViewById( R.id.dropdown_arrow );
        ImageView dropDownArrowUp = (ImageView) findViewById( R.id.dropdown_arrow_up );
        authorOne.setVisibility( View.GONE);
        authorTwo.setVisibility( View.GONE);
        reviewOne.setVisibility( View.GONE);
        reviewTwo.setVisibility( View.GONE);
        dropDownArrow.setVisibility( View.VISIBLE );
        dropDownArrowUp.setVisibility( View.GONE );
        detailsInt = 0;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt( "Reviews State", detailsInt );
        super.onSaveInstanceState( outState );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        detailsInt = savedInstanceState.getInt( "Reviews State" );
        if (detailsInt==1){
            revealReviews();
        }
        else if (detailsInt==0) {
        hideReviews();
        }
        super.onRestoreInstanceState( savedInstanceState );
    }
}
