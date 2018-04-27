package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import com.example.android.popularmovies.FavouritesDatabase.FavouritesContract.FavouritesTable;

import static android.provider.Settings.Global.getString;

/**
 * Created by scoll on 27/02/2018.
 */

public class FilmData {

    private static final String LOG_TAG = FilmData.class.getSimpleName();
    public static List<Film> filmsForImageAdapter;

    //Fetches the films that will populate the grid. Calls the createURL method and extractFeaturesFromJson method.
    public static List<Film> fetchFilms(String requestUrl) {
        URL url = createURL( requestUrl );
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest( url );
        } catch (IOException e) {
            Log.e( LOG_TAG, "Problem making the htpp request" );
        }
        List<Film> films = extractFeaturesFromJson( jsonResponse );
        return films;
    }

    public static List<YoutubeJSON> fetchYoutubeKeys(String youtubeJSON) {
        URL url = createURL( youtubeJSON );
        String youtubeJsonResponse = null;
        try {
            youtubeJsonResponse = makeHttpRequest( url );
        } catch (IOException e) {
            Log.e( LOG_TAG, "Problem making the htpp request" );
        }
        List<YoutubeJSON> keys = extractKeysFromJson( youtubeJsonResponse );
        return keys;
    }

    public static List<ReviewsJSON> fetchReviews(String reviewsJSON) {
        URL url = createURL( reviewsJSON );
        String reviewsJsonResponse = null;
        try {
            reviewsJsonResponse = makeHttpRequest( url );
        } catch (IOException e) {
            Log.e( LOG_TAG, "Problem making the htpp request" );
        }
        List<ReviewsJSON> reviews = extractReviewsFromJson( reviewsJsonResponse );
        return reviews;
    }


    public static List <ReviewsJSON> extractReviewsFromJson(String reviewsJsonResponse){
        List <ReviewsJSON> reviews = new ArrayList<ReviewsJSON>(  );
        if (reviewsJsonResponse!=null) {
            try {
                JSONObject reviewsJsonFile = new JSONObject( reviewsJsonResponse);
                if (reviewsJsonFile.length() == 0) {
                    reviews.add( new ReviewsJSON( "N/A", "No review yet") );
                    reviews.add( new ReviewsJSON( "N/A", "No review yet") );
                } else {
                    JSONArray jsonReviewsArray = reviewsJsonFile.getJSONArray( "results" );
                    int b = 2;
                    if (jsonReviewsArray.length() == 0) {
                        reviews.add( new ReviewsJSON( "N/A", "No review yet") );
                        reviews.add( new ReviewsJSON( "N/A", "No review yet") );
                    } else if (jsonReviewsArray.length() < b) {
                        for (int i = 0; i < b; i++) {
                            JSONObject newReview = jsonReviewsArray.getJSONObject( 0 );
                            String author = newReview.getString( "author" );
                            String review = newReview.getString( "content");
                            reviews.add( new ReviewsJSON( author, review) );
                        }
                    } else {
                        for (int i = 0; i < b; i++) {
                            JSONObject newReview = jsonReviewsArray.getJSONObject( i);
                            String author = newReview.getString( "author" );
                            String review = newReview.getString( "content");
                            reviews.add( new ReviewsJSON( author, review) );
                        }
                    }
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
        }
        else if (reviewsJsonResponse==null){
            reviews.add( new ReviewsJSON( "N/A", "No review yet") );
            reviews.add( new ReviewsJSON( "N/A", "No review yet") );
        }
        return reviews;
    }


    public static List <YoutubeJSON> extractKeysFromJson (String youtubeJsonResponse){
        List <YoutubeJSON> keys = new ArrayList<YoutubeJSON>(  );
        if (youtubeJsonResponse!=null) {
            try {
                JSONObject keysJsonFile = new JSONObject( youtubeJsonResponse );
                if (keysJsonFile.length() == 0) {
                    keys.add( new YoutubeJSON( "void" ) );
                    keys.add( new YoutubeJSON( "void" ) );
                } else {
                    JSONArray jsonKeyArray = keysJsonFile.getJSONArray( "results" );
                    int b = 2;
                    if (jsonKeyArray.length() == 0) {
                        keys.add( new YoutubeJSON( "void" ) );
                        keys.add( new YoutubeJSON( "void" ) );
                    } else if (jsonKeyArray.length() < b) {
                        for (int i = 0; i < b; i++) {
                            JSONObject newVideo = jsonKeyArray.getJSONObject( 0 );
                            String youtubeKey = newVideo.getString( "key" );
                            keys.add( new YoutubeJSON( youtubeKey ) );
                        }
                    } else {
                        for (int i = 0; i < b; i++) {
                            JSONObject newVideo = jsonKeyArray.getJSONObject( i );
                            String youtubeKey = newVideo.getString( "key" );
                            keys.add( new YoutubeJSON( youtubeKey ) );
                        }
                    }
                }
                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        else if (youtubeJsonResponse==null){
            keys.add(new YoutubeJSON( "void" ));
            keys.add(new YoutubeJSON( "void" ));
        }
        return keys;
    }

    //Takes the information from the JSON file and creates a new film for each new entry and stores the relevant information about that film within the new object.
    public static List<Film> extractFeaturesFromJson (String jsonResponse){
        List <Film> films = new ArrayList<Film>();
        try {
            JSONObject filmsJsonFile = new JSONObject(jsonResponse);
            JSONArray filmsJsonArray = filmsJsonFile.getJSONArray( "results" );
            for (int i=0; i<filmsJsonArray.length(); i++){
                JSONObject newFilm = filmsJsonArray.getJSONObject( i );
                String filmID = newFilm.getString( "id" );
                String filmName = newFilm.getString( "title" );
                String filmSummary = newFilm.getString( "overview" );
                int filmRating = newFilm.getInt( "vote_average" );
                String filmRelease = newFilm.getString( "release_date" );
                String filmImageUrl = newFilm.getString( "poster_path" );
                String youtubeJSONFile = youtubeLinkFinder(filmID);
                List<YoutubeJSON> youtubeKeys = fetchYoutubeKeys( youtubeJSONFile );
                String youtubeOne = "void";
                String youtubeTwo = "void";
                if (youtubeKeys.size()>0) {
                    youtubeOne = youtubeKeys.get( 0 ).getYoutubeKey();
                    youtubeTwo = youtubeKeys.get( 1 ).getYoutubeKey();
                }
                String reviewsJSONFile = reviewsLinkFinder(filmID);
                List<ReviewsJSON> reviews = fetchReviews( reviewsJSONFile );
                String authorOne = "N/A";
                String authorTwo = "N/A";
                String reviewOne = "N/A";
                String reviewTwo = "N/A";
                if (reviews.size()>0){
                    authorOne = reviews.get(0).getAuthor();
                    reviewOne = reviews.get( 0 ).getReview();
                    authorTwo = reviews.get(1).getAuthor();
                    reviewTwo = reviews.get( 1 ).getReview();
                }

                films.add(new Film (filmName, filmRelease, filmRating, filmSummary, filmImageUrl, filmID, youtubeOne, youtubeTwo, authorOne, reviewOne, authorTwo, reviewTwo));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        filmsForImageAdapter = films;
        return films;
    }

    //Attempts to connect to the internet using the URL provided. If the connection is unavailable, the jsonResponse will be blank. Uses the readFromStream method to generate the response.
    private static String makeHttpRequest (URL url) throws IOException{
        String jsonResponse = "";
        if (url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        int urlResponse=0;
        try{ 
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout( 150000 );
            urlConnection.setReadTimeout( 100000 );
            urlConnection.setRequestMethod( "GET" );
            urlConnection.connect();
            if (urlConnection.getResponseCode()<300){
                inputStream=urlConnection.getInputStream();
                jsonResponse= readFromStream (inputStream);
                }
            else if (urlConnection.getResponseCode()==429){
                urlConnection.disconnect();
                urlResponse=429;
                }

            else {
                Log.e( LOG_TAG, "Problem with the URL connection " + urlConnection.getResponseCode() + urlConnection.getInstanceFollowRedirects());
            }
        }
        catch (IOException e) {
            Log.e( LOG_TAG, "Problem getting the JSON file" );
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null && urlResponse!=429) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Creates the jsonResponse by appending each new paramater retrieved.
    private static String readFromStream (InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder(  );
        if (inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader( inputStream, Charset.forName ("UTF-8") );
            BufferedReader bufferedReader = new BufferedReader( inputStreamReader );
            String line = bufferedReader.readLine();
            while (line!=null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }


    //Turns the String query into a URL. If there are any issues, returns null.
    private static URL createURL (String stringUrl){
        URL url = null;
        try{
            url = new URL (stringUrl);
        }
        catch (MalformedURLException e){
            Log.e (LOG_TAG, "Problem generating the URL");
        }
        return url;
    }

    public static List<Film> extractFeaturesFromFavouriteFilmsCursor(Cursor cursor) {

            List <Film> films = new ArrayList<Film>();
            if (cursor!=null){
                for (int i=0; i<cursor.getCount(); i++){
                    cursor.moveToPosition( i );
                    String filmID = cursor.getString(cursor.getColumnIndex( FavouritesTable.COLUMN_FILM_ID ) );
                    String filmName = cursor.getString(cursor.getColumnIndex( FavouritesTable.COLUMN_FILM_NAME ) );
                    String filmSummary = cursor.getString(cursor.getColumnIndex( FavouritesTable.COLUMN_DESC) );
                    int filmRating = cursor.getInt(cursor.getColumnIndex( FavouritesTable.COLUMN_RATING ) );
                    String filmRelease = cursor.getString(cursor.getColumnIndex( FavouritesTable.COLUMN_DATE_RELEASED ) );
                    String filmImageUrl = cursor.getString(cursor.getColumnIndex( FavouritesTable.COLUMN_IMAGE) );
                    String youtubeOne = cursor.getString(cursor.getColumnIndex( FavouritesTable.COLUMN_YOUTUBE_ONE ));
                    String youtubeTwo = cursor.getString(cursor.getColumnIndex( FavouritesTable.COLUMN_YOUTUBE_TWO ));
                    String authorOne = cursor.getString(cursor.getColumnIndex( FavouritesTable.COLUMN_AUTHOR_ONE ));
                    String reviewOne = cursor.getString(cursor.getColumnIndex( FavouritesTable.COLUMN_REVIEW_ONE));
                    String authorTwo = cursor.getString(cursor.getColumnIndex( FavouritesTable.COLUMN_AUTHOR_TWO));
                    String reviewTwo = cursor.getString(cursor.getColumnIndex( FavouritesTable.COLUMN_REVIEW_TWO));
                    films.add(new Film (filmName, filmRelease, filmRating, filmSummary, filmImageUrl, filmID, youtubeOne, youtubeTwo, authorOne, reviewOne, authorTwo, reviewTwo));
                }}
            filmsForImageAdapter = films;
            return films;
        }

    public static String youtubeLinkFinder (String id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MainActivity.getYoutubeOne());
        stringBuilder.append(id);
        stringBuilder.append( MainActivity.getYoutubeTwo());
        stringBuilder.append((BuildConfig.API_KEY).toString());
        return stringBuilder.toString();
    }

    public static String reviewsLinkFinder (String id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MainActivity.getReviewsOne());
        stringBuilder.append(id);
        stringBuilder.append( MainActivity.getReviewsTwo());
        stringBuilder.append((BuildConfig.API_KEY).toString());
        return stringBuilder.toString();
    }

    }
