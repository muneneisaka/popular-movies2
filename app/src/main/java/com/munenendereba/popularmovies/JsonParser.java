package com.munenendereba.popularmovies;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class JsonParser {
    public String parseJSon(String incomingUrl) {
        HttpsURLConnection connection = null;
        try {
            URL url = new URL(incomingUrl);
            connection = (HttpsURLConnection) url.openConnection();

            connection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (connection != null) connection.disconnect();

        return null;
    }

    public ArrayList<String> returnMoviePosterList(String incomingString) {

        HttpsURLConnection connection = null;
        try {
            URL url = new URL(incomingString);
            connection = (HttpsURLConnection) url.openConnection();

            connection.connect();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            ArrayList<String> outputString = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
                outputString.add(line);
            }
            bufferedReader.close();

            //return outputString;
            ArrayList<String> moviePosters = new ArrayList<>();

           /* try {
                JSONObject jb = new JSONObject(outputString.toString());
                JSONArray results = jb.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject theMovie = results.getJSONObject(i);
                    moviePosters.add(theMovie.getString("poster_path"));
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            Log.i("returned-posters", moviePosters.toString());
            return moviePosters;*/

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (connection != null) connection.disconnect();

        return null;
    }

    public ArrayList<String> returnMoviePosters(String s) {
        //String movies = getResources().
        ArrayList<String> moviePosters = new ArrayList<>();
        String l = s.replaceAll("'", "\'");

        try {
            //JSONObject jb = new JSONObject(s);
            JSONArray js = new JSONArray(l.toString());


            //JSONArray results = jb.getJSONArray("results");
            for (int i = 0; i < js.length(); i++) {
                JSONObject jb = js.getJSONObject(i);

                //JSONObject theMovie = results.getJSONObject(i);
                moviePosters.add(jb.getString("poster_path"));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        Log.i("returned-posters", moviePosters.toString());
        return moviePosters;
    }


}
