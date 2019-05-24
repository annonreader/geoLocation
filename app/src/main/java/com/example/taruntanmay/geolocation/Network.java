package com.example.taruntanmay.geolocation;

import android.net.Uri;
import android.text.format.Time;
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
 class Network  {
    private static final String LOG_TAG = Network.class.getSimpleName();


    static String temp;
    static String min;
    static String max;

    public String connect(String lat,String lon)
    {
        Log.e("inside connect","we have received the call "+lat+" "+lon);
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    String ret="";
    // Will contain the raw JSON response as a string.
    String forecastJsonStr = null;

    String format = "json";
    String units = "metric";
    int numDays = 1;

        try {

        final String FORECAST_BASE_URL =
                "http://api.openweathermap.org/data/2.5/weather?";
        final String QUERY_PARAM = "lat";
        final String QUERY2_PARAM = "lon";
        final String FORMAT_PARAM = "mode";
        final String UNITS_PARAM = "units";
        final String DAYS_PARAM = "cnt";
        final String APPID_PARAM = "APPID";

        Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, lat)
                .appendQueryParameter(QUERY2_PARAM,lon)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .appendQueryParameter(APPID_PARAM, BuildConfig.weatherapi)
                .build();

        URL url = new URL(builtUri.toString());
        Log.e("url",builtUri.toString());

        // Create the request to OpenWeatherMap, and open the connection
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.connect();

        // Read the input stream into a String
        InputStream inputStream = urlConnection.getInputStream();
        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            // Nothing to do.
            return null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {

            buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
            return null;
        }
         forecastJsonStr = buffer.toString();
        ret = getWeatherDataFromJson(forecastJsonStr);
    } catch (IOException e) {
        Log.e(LOG_TAG, "Error ", e);

    } catch (JSONException e) {
        Log.e(LOG_TAG, e.getMessage(), e);
        e.printStackTrace();
    } finally {
        if (urlConnection != null) {
            urlConnection.disconnect();
        }
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
                Log.e(LOG_TAG, "Error closing stream", e);
            }
        }
    }
        return ret;
}
    private String getWeatherDataFromJson(String forecastJsonStr)
            throws JSONException {


        final String OWM_CITY = "main";

        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "temp_max";
        final String OWM_MIN = "temp_min";



        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);

            JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
            temp = cityJson.getString(OWM_TEMPERATURE);
             max = cityJson.getString(OWM_MAX);
             min = cityJson.getString(OWM_MIN);


            Log.e("temp",temp+" "+max+" "+min);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return temp+" "+max+" "+min;
    }

}