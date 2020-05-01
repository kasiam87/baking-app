package com.example.android.backingapp.api;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import static com.example.android.backingapp.api.JsonParser.RECIPES_BASE_URI;
import static com.example.android.backingapp.api.JsonParser.RECIPES_PATH;


//TODO use retrofit
//I highly recommend using Retrofit. it's a type-safe HTTP client for Android and Java.
//
//Retrofit will save your development time, And also you can keep your code in developer friendly.
// Retrofit has given almost all the API's to make server call and to receive response.
// internally they also use GSON to do the parsing.
// you can go through this link you will get more info http://vickychijwani.me/retrofit-vs-volley/
public class NetworkHelper {

    public static URL buildUrl() {
        Uri builtUri = Uri.parse(RECIPES_BASE_URI)
                .buildUpon()
                .appendEncodedPath(RECIPES_PATH)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
