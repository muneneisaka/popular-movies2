package com.munenendereba.popularmovies;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

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
}
