package com.example.android.popularmovies;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.view.View.VISIBLE;
import static com.example.android.popularmovies.FavouritesDatabase.FavouritesContract.FavouritesTable.CONTENT_URI;
import static com.example.android.popularmovies.R.layout.loading;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Film>> {

    // Variables required to be accessible outside of functions.
    private static final int MOVIE_LOADER_ID = 0;
    private int loaderChecker = 0;
    private String moviesJSON;
    private NetworkInfo isNetworkActive;
    private static int PAGE_TO_LOAD = 0;
    private String sortByPopularity;
    private String sortByRating;
    public static List<Film> films;
    public static Cursor cursor;
    public static String mYoutubeOne;
    public static String mYoutubeTwo;
    public static String mReviewsOne;
    public static String mReviewsTwo;
    public static String mYoutubeBaseQuery;
    public static int indexFav = 0;
    public static int indexPop = 0;
    public static int indexRate = 0;
    GridView gridView;
    int contentRetrieved;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        cursor = getContentResolver().query( CONTENT_URI, null, null, null, null );
        setYoutubeTwo();
        setYoutubeOne();
        setYoutubeBaseQuery();
        setReviewsOne();
        setReviewsTwo();


        //Generate the API queries using the API key. This query will not work unless an API key is added to the Strings resource directory.
        sortByRating = rateSortQuery();
        sortByPopularity = popSortQuery();

        //Layout is set to default open the loading screen.
        loadingScreen( true );

        //Attempts to load a new instance of the lists, if it connects, it will set the layout to the main activity. Otherwise it will idol on the loading screen until a connection is made.
        films = newFilms();
        newConnection();

    }

    //If films does not exist already, generates new films list. Otherwise this clears the list and then generates a new list via a JSON file.
    public ArrayList<Film> newFilms() {
        if (films != null) {
            films.clear();
        }

        if (PAGE_TO_LOAD == 0 || PAGE_TO_LOAD == 1) {
            if (PAGE_TO_LOAD == 0) {
                moviesJSON = sortByPopularity;
            } else if (PAGE_TO_LOAD == 1) {
                moviesJSON = sortByRating;
            }
            final ArrayList<Film> newFilmList = new ArrayList<Film>( FilmData.extractFeaturesFromJson( moviesJSON ) );
            return newFilmList;
        } else {
            cursor = getContentResolver().query( CONTENT_URI, null, null, null, null );
            final ArrayList<Film> newFavouriteFilms = new ArrayList<Film>( FilmData.extractFeaturesFromFavouriteFilmsCursor( cursor ) );
            return newFavouriteFilms;
        }

    }


    //Checks the current connection status to determine if the rest of the program is able to execute.
    public void newConnection() {
        //Connectivity Manager, determines if there is internet connectivity
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService( Context.CONNECTIVITY_SERVICE );
        isNetworkActive = cm.getActiveNetworkInfo();
        if (isNetworkActive != null && isNetworkActive.isConnectedOrConnecting()) {
            if (loaderChecker >= 1) {
                getLoaderManager().destroyLoader( MOVIE_LOADER_ID );
                loaderChecker--;
            }
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader( MOVIE_LOADER_ID, null, this );
            loaderChecker++;
        } else if (isNetworkActive == null) {
            int id = R.id.sort_by_menu_three;
            PAGE_TO_LOAD = 2;
            films = newFilms();
            if (loaderChecker >= 1) {
                getLoaderManager().destroyLoader( MOVIE_LOADER_ID );
                loaderChecker--;
            }
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader( MOVIE_LOADER_ID, null, this );
            loaderChecker++;
        }
    }

    //Generates a menu which will allow the user to sort by popularity or rating.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.sort_menu, menu );
        return true;
    }

    //Allows the user to perform a search for most popular and highest rated films. When a menu item is clicked, it saves the last search type as an integer, which will allow
    // users to continue where they left off (once they have clicked on a poster for further film details.
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.sort_by_menu_one || id == R.id.sort_by_menu_two) {
            if (id == R.id.sort_by_menu_one) {
                PAGE_TO_LOAD = 0;
                moviesJSON = sortByPopularity;
            } else if (id == R.id.sort_by_menu_two) {
                PAGE_TO_LOAD = 1;
                moviesJSON = sortByRating;
            }
            loadingScreen( true );
            films = newFilms();
            newConnection();
        } else if (id == R.id.sort_by_menu_three) {
            PAGE_TO_LOAD = 2;
            films = newFilms();
            if (loaderChecker >= 1) {
                getLoaderManager().destroyLoader( MOVIE_LOADER_ID );
                loaderChecker--;
            }
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader( MOVIE_LOADER_ID, null, this );
            loaderChecker++;
        }
        return super.onOptionsItemSelected( menuItem );
    }

    //Create a new loader to generate the grid off of the main thread.
    @Override
    public Loader<List<Film>> onCreateLoader(int i, Bundle bundle) {
        contentRetrieved = 0;
        if (PAGE_TO_LOAD != 2) {
            return new FilmLoader( this, moviesJSON );
        } else {
            return new FilmLoader( this );
        }
    }

    //When loader has finished set the information into the grid.
    @Override
    public void onLoadFinished(Loader<List<Film>> loader, List<Film> newfilms) {
        films = newfilms;
        //Create the gridView and attach the ImageAdapter class which populates each of the images with images from the API. At present this just uses an array of placeholders.
        if (films.size() != 0) {
            setContentView( R.layout.activity_main );
            gridView = (GridView) findViewById( R.id.gridview );
            gridView.setAdapter( new ImageAdapter( this ) );
        } else if (PAGE_TO_LOAD == 2 && films.size() == 0) {
            setContentView( loading );
            TextView noFavs = (TextView) findViewById( R.id.no_favourites );
            TextView noInternet = (TextView) findViewById( R.id.no_internet );
            noFavs.setVisibility( VISIBLE );
            noInternet.setVisibility( View.GONE );
        }
        contentRetrieved = 1;
    }

    //Standard loaderReset function.
    @Override
    public void onLoaderReset(Loader<List<Film>> loader) {
    }

    //Allows films to be retrieved from other classes
    public static List sharingArrays() {
        return films;
    }


    //Generates the popularity search query using the information stored in the Strings resource file.
    public String popSortQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( getString( R.string.base_Query ) );
        stringBuilder.append( getString( R.string.pop_Sort ) );
        stringBuilder.append( (BuildConfig.API_KEY).toString() );
        return stringBuilder.toString();
    }

    //Generates the ratings search query using the information stored in the Strings resource file.
    public String rateSortQuery() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append( getString( R.string.base_Query ) );
        stringBuilder.append( getString( R.string.rate_Sort ) );
        stringBuilder.append( (BuildConfig.API_KEY).toString() );
        return stringBuilder.toString();
    }

    public void setYoutubeOne() {
        String youtubeOne = getString( R.string.findYoutubeIdQueryBuilderPartOne );
        mYoutubeOne = youtubeOne;
    }

    public void setYoutubeTwo() {
        String youtubeTwo = getString( R.string.findYoutubeIdQueryBuilderPartTwo );
        mYoutubeTwo = youtubeTwo;
    }

    public void setYoutubeBaseQuery() {
        String youtubeBase = getString( R.string.youtubeBaseQuery );
        mYoutubeBaseQuery = youtubeBase;
    }

    public static String getYoutubeOne() {
        return mYoutubeOne;
    }

    public static String getYoutubeTwo() {
        return mYoutubeTwo;
    }

    public static String getYoutubeBaseQuery() {
        return mYoutubeBaseQuery;
    }

    public void setReviewsOne() {
        String reviewsOne = getString( R.string.reviewsStringBuilderOne );
        mReviewsOne = reviewsOne;
    }

    public void setReviewsTwo() {
        String reviewsTwo = getString( R.string.reviewsStringBuilderTwo );
        mReviewsTwo = reviewsTwo;
    }

    public static String getReviewsOne() {
        return mReviewsOne;
    }

    public static String getReviewsTwo() {
        return mReviewsTwo;
    }

    public void loadingScreen(Boolean show) {
        if (show == true) {
            setContentView( loading );
            View loadingIndicator = findViewById( R.id.loadingSpinner );
            loadingIndicator.setVisibility( VISIBLE );
        }
    }

    @Override
    protected void onStart() {
        if (contentRetrieved==1){
            onResume();
        }
        else {
        contentRetrieved = 0;
        loadingScreen( true );
        films = newFilms();
        newConnection();
        }
        super.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (films.isEmpty()!=true){
        outState.putParcelableArrayList( "Films To Persist", new ArrayList<Film>( films ) );
        switch (PAGE_TO_LOAD) {
            case 0:
                indexPop = gridView.getLastVisiblePosition();
                outState.putInt( "Index", indexPop );
                outState.putInt( "Page To Load", PAGE_TO_LOAD );
                break;
            case 1:
                indexRate = gridView.getFirstVisiblePosition();
                outState.putInt( "Index", indexRate);
                outState.putInt( "Page To Load", PAGE_TO_LOAD );
                break;
            case 2:
                indexFav = gridView.getFirstVisiblePosition();
                outState.putInt( "Index", indexFav);
                outState.putInt( "Page To Load", PAGE_TO_LOAD );
                break;
        }}
        super.onSaveInstanceState( outState );
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );
        List<Film> restoreFilms = savedInstanceState.getParcelableArrayList( "Films To Persist" );
        if (restoreFilms.isEmpty() != true) {
            films = savedInstanceState.getParcelableArrayList( "Films To Persist" );
            if (films.size() != 0) {
                setContentView( R.layout.activity_main );
                gridView = (GridView) findViewById( R.id.gridview );
                gridView.setAdapter( new ImageAdapter( this ) );
            } else if (PAGE_TO_LOAD == 2 && films.size() == 0) {
                setContentView( loading );
                TextView noFavs = (TextView) findViewById( R.id.no_favourites );
                TextView noInternet = (TextView) findViewById( R.id.no_internet );
                noFavs.setVisibility( VISIBLE );
                noInternet.setVisibility( View.GONE );
            }
            int loadPage = savedInstanceState.getInt( "PAGE_TO_LOAD" );
            switch (loadPage) {
                case 0:
                    int orientation = this.getResources().getConfiguration().orientation;
                    int newIndex = 0;
                    indexPop = savedInstanceState.getInt( "Index" );
                    if (orientation==2){
                        if (indexPop<=4){
                            newIndex = 0;
                        }
                        else if (indexPop>=5){
                            newIndex = indexPop-5;
                        }
                    } else if (orientation==1){
                        if (indexPop<=6){
                            newIndex = 0;
                        }
                        else if (indexPop>=7){
                            newIndex = indexPop-3;
                        }
                }
                    gridView.setSelection( newIndex );
                    break;
                case 1:
                    indexRate = savedInstanceState.getInt( "Index" );
                    gridView.setSelection( indexRate );
                    break;
                case 2:
                    indexFav = savedInstanceState.getInt( "Index" );
                    gridView.setSelection( indexFav );
                    break;
            }
        }
        else {onStart();}
        }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        List<Film> restoreFilms = FilmData.filmsForImageAdapter;
        if (restoreFilms.isEmpty() != true) {
            films = restoreFilms;
            if (films.size() != 0) {
                setContentView( R.layout.activity_main );
                gridView = (GridView) findViewById( R.id.gridview );
                gridView.setAdapter( new ImageAdapter( this ) );
            } else if (PAGE_TO_LOAD == 2 && films.size() == 0) {
                setContentView( loading );
                TextView noFavs = (TextView) findViewById( R.id.no_favourites );
                TextView noInternet = (TextView) findViewById( R.id.no_internet );
                noFavs.setVisibility( VISIBLE );
                noInternet.setVisibility( View.GONE );
            }
            switch (PAGE_TO_LOAD) {
                case 0:
                    int orientation = this.getResources().getConfiguration().orientation;
                    int newIndex = 0;
                    if (orientation==2){
                        if (indexPop<=4){
                            newIndex = 0;
                        }
                        else if (indexPop>=5){
                            newIndex = indexPop-5;
                        }
                    } else if (orientation==1){
                        if (indexPop<=6){
                            newIndex = 0;
                        }
                        else if (indexPop>=7){
                            newIndex = indexPop-3;
                        }
                    }
                    gridView.setSelection( newIndex );
                    break;
                case 1:
                    gridView.setSelection( indexRate );
                    break;
                case 2:
                    PAGE_TO_LOAD = 2;
                    films = newFilms();
                    if (loaderChecker >= 1) {
                        getLoaderManager().destroyLoader( MOVIE_LOADER_ID );
                        loaderChecker--;
                    }
                    LoaderManager loaderManager = getLoaderManager();
                    loaderManager.initLoader( MOVIE_LOADER_ID, null, this );
                    loaderChecker++;
                    gridView.setSelection( indexFav );
                    break;
            }
        }
        else {onStart();}
        super.onResume();
    }
}
