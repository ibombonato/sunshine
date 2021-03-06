package br.com.icarobombonato.sunshine;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Icaro on 26/07/2014.
 */
public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

    private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    WeatherFormatter weatherFormatter = new DailyWeatherFormatter();

    // Will contain the raw JSON response as a string.
    String forecastJsonStr = null;

    @Override
    protected String[] doInBackground(String... params){
        GetData(params);
        try {
            return GetWeatherStringArray();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Void GetData(String... params) {

        try {

            Uri.Builder uriBuilder = new Uri.Builder()
                    .scheme("http")
                    .encodedAuthority("api.openweathermap.org")
                    .appendPath("data")
                    .appendPath("2.5")
                    .appendPath("forecast")
                    .appendPath("daily")
                    .appendQueryParameter("q", params[0])
                    .appendQueryParameter("mode", "json")
                    .appendQueryParameter("units", params[1])
                    .appendQueryParameter("cnt", "7");

            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7");
            String builderString = uriBuilder.build().toString();
            URL url = new URL(builderString);

            Log.v(LOG_TAG, "Builder URI: " + builderString);

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
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            forecastJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e("ForecastFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("ForecastFragment", "Error closing stream", e);
                }
            }
        }
        return null;
    }

    public String[] GetWeatherStringArray() throws JSONException {
        return weatherFormatter.getWeatherDataFromJson(forecastJsonStr, 7);
    }

    @Override
    protected void onPostExecute(String[] result) {
        if (result != null){
            ForecastFragment.mForecastArrayAdapter.clear();
            for (String dayForecast : result){
                ForecastFragment.mForecastArrayAdapter.add(dayForecast);
            }
        }
    }
}
