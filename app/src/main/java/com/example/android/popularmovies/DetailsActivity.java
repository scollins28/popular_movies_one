package com.example.android.popularmovies;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by scoll on 27/02/2018.
 */

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.details_activity );

        //Pulls in the JSON information from the main activity.
        List<Film> imageAdapterfilms = MainActivity.sharingArrays();

        //Finds which film the information is related to.
        int filmNumber = getIntent().getIntExtra("Position", 0);
        Film currentFilm = imageAdapterfilms.get(filmNumber);

        //Calls all of the film data methods to retrieve the information particular to this film.
        ImageView movieImage = (ImageView) findViewById( R.id.details_image );
        TextView movieTitle = (TextView) findViewById( R.id.details_title);
        TextView movieRelease = (TextView) findViewById( R.id.details_release_date);
        TextView movieSynopsis = (TextView) findViewById( R.id.details_synopsis);
        RatingBar movieRating = (RatingBar) findViewById( R.id.details_rating);


        //Uses the information retrieved above to set the data of the details activity.
        movieTitle.setText( currentFilm.getMovieName());
        movieRelease.setText( currentFilm.getmMovieReleaseDate());
        movieSynopsis.setText( currentFilm.getMovieSummary());
        movieRating.setNumStars( (int) currentFilm.getMovieRating() );
        String moviePicassoImage = "http://image.tmdb.org/t/p/w500/".concat( currentFilm.getMovieImageUrl() );
        Picasso.with(this).load(moviePicassoImage).into(movieImage);

        setTitle(currentFilm.getMovieName());

    }
}
