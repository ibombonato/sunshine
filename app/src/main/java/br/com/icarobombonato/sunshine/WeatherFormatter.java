package br.com.icarobombonato.sunshine;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by icaro on 30/07/2014.
 */
public abstract class WeatherFormatter {
    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */
    protected String getReadableDateString(long time){
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        return format.format(date).toString();
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    protected String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    public abstract String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException;
}
