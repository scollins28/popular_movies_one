package com.example.android.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by scoll on 22/02/2018.
 */
public class ImageAdapter extends BaseAdapter {

    //Variable to hold context
    private Context mContext;

    //Method to set context variable mContext
    public ImageAdapter(Context c) {
        mContext = c;
    }

    //Method to return the total number of images to be contained within the GridView
    public int getCount() {
        return mThumbIds.length;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;

        //If there is no view that can be recycled, create new view with the following specifications.
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setPadding( 8,8,8,8 );
        }
        //If there is a recyclable view avialable imageView = that view.
        else {
            imageView = (ImageView) convertView;
        }

        //Set imageView to be at the resource at the correspodning position in the array of images.
        //Then return the view.
        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    //Temporary arry of images to act as placeholders until actual data is pulled through.
    private Integer[] mThumbIds = {
            R.drawable.movone, R.drawable.movtwo,
            R.drawable.movthree, R.drawable.movfive,
            R.drawable.movsix, R.drawable.movfour,
            R.drawable.movseven, R.drawable.moveight,
            R.drawable.movnine, R.drawable.movten,
            R.drawable.movone, R.drawable.movtwo,
            R.drawable.movthree, R.drawable.movfive,
            R.drawable.movsix, R.drawable.movfour,
            R.drawable.movseven, R.drawable.moveight,
            R.drawable.movnine, R.drawable.movten,
            R.drawable.movnine, R.drawable.movten,
    };
}