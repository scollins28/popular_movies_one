package com.example.android.popularmovies;

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

/**
 * Created by scoll on 27/02/2018.
 */

public class FilmData {

    private static final String LOG_TAG = FilmData.class.getSimpleName();
    public static List<Film> filmsForImageAdapter;

    //Fetches the films that will populate the grid. Calls the createURL method and extractFeaturesFromJson method.
        public static List<Film> fetchFilms (String requestUrl){
            URL url = createURL(requestUrl);
            String jsonResponse = null;
            try{
                jsonResponse = makeHttpRequest (url);
            }
            catch (IOException e){
                Log.e( LOG_TAG, "Problem making the htpp request" );
            }
            List <Film> films = extractFeaturesFromJson( jsonResponse );
            return films;
        }


    //Takes the information from the JSON file and creates a new film for each new entry and stores the relevant information about that film within the new object.
    public static List<Film> extractFeaturesFromJson (String jsonResponse){
        List <Film> films = new ArrayList<Film>();
        try {
            JSONObject filmsJsonFile = new JSONObject(jsonResponse);
            JSONArray filmsJsonArray = filmsJsonFile.getJSONArray( "results" );
            for (int i=0; i<filmsJsonArray.length(); i++){
                JSONObject newFilm = filmsJsonArray.getJSONObject( i );
                String filmName = newFilm.getString( "title" );
                String filmSummary = newFilm.getString( "overview" );
                int filmRating = newFilm.getInt( "vote_average" );
                String filmRelease = newFilm.getString( "release_date" );
                String filmImageUrl = newFilm.getString( "poster_path" );
                films.add(new Film (filmName, filmRelease, filmRating, filmSummary, filmImageUrl));
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
            if (inputStream != null) {
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

}
