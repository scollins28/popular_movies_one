package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scoll on 22/02/2018.
 */
public class ImageAdapter extends BaseAdapter {

    List<Film> imageAdapterfilms = MainActivity.sharingArrays();

    //Variable to hold context
    private Context mContext;

    //Method to set context variable mContext
    public ImageAdapter(Context c) {
        mContext = c;
    }

    //Method to return the total number of images to be contained within the GridView
    public int getCount() {
        return imageAdapterfilms.size();
    }

    //Required method, unedited from standard import
    public Object getItem(int position) {
        return null;
    }

    //Required method, unedited from standard import
    public long getItemId(int position) {
        return 0;
    }

    //Method to create a new ImageView for each movie image
    public View getView(final int position, final View convertView, ViewGroup parent) {
        ImageView imageView;
        imageView = new ImageView( mContext );

        if (position< imageAdapterfilms.size()) {

            //If there is no view that can be recycled, create new view with the following specifications.
            if (convertView == null) {
                imageView.setLayoutParams( new GridView.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT ) );
                imageView.setPadding( 16, 8, 16, 8 );
            }
            //If there is a recyclable view avialable imageView = that view.
            else {
                imageView = (ImageView) convertView;
            }

            //Set imageView to be at the resource at the correspodning position in the array of images.
            //Then return the view.
            Film currentFilm = imageAdapterfilms.get( position );
            String moviePicassoImage = "http://image.tmdb.org/t/p/w342/".concat( currentFilm.getMovieImageUrl() );
            Picasso.with( mContext ).load( moviePicassoImage ).into( imageView );


            //Set the all imageViews to have an onClickListener that takes them to the placeholder Details Activity class.
            imageView.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent( mContext, DetailsActivity.class );
                    intent.putExtra( "Position", position );
                    mContext.startActivity( intent );
                }
            } );
        }
            //Return the ImageView to complete the function.
            return imageView;

    }
}