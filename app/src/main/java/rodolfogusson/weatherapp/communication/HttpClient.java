package rodolfogusson.weatherapp.communication;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.preference.PreferenceManager;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import rodolfogusson.weatherapp.R;

/**
 * Created by rodolfo on 5/17/17.
 */

class HttpClient {
    private static String WEATHER_NOW_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String FORECAST_BASE_URL = "http://api.openweathermap.org/data/2.5/forecast/daily?q=";
    private static String CITIES_BASE_URL = "http://api.openweathermap.org/data/2.5/find?q=";
    private static String LAT_LON_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static String IMG_URL = "http://openweathermap.org/img/w/";
    private static String apiKey = "";
    private static String APP_ID_SUFFIX = "&APPID=";
    private Context context;

    HttpClient(Context ctx){
        this.context = ctx;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        apiKey = prefs.getString(context.getString(R.string.key_api_key),null);
    }

    String getWeatherNow(String location){
        return getWeatherData(location, WEATHER_NOW_BASE_URL,"");
    }

    String getWeatherForecast(String location){
        return getWeatherData(location, FORECAST_BASE_URL,"&cnt=16");
    }

    private String requestData(String url){
        HttpURLConnection con = null;
        InputStream is = null;
        try {
            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            int responseCode = con.getResponseCode();
            if (responseCode >= 400 && responseCode <= 499) {
                throw new Exception("Bad authentication status: " + responseCode);
            } else {
                StringBuffer buffer = new StringBuffer();
                is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while ((line = br.readLine()) != null)
                    buffer.append(line + "rn");
                is.close();
                con.disconnect();
                return buffer.toString();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }
        return null;
    }

    private String getWeatherData(String location, String baseURL, String countSuffix) {
        return requestData(baseURL + location + countSuffix + APP_ID_SUFFIX + apiKey);
    }

    String getCitiesData(String cityNamePattern){
        return requestData(CITIES_BASE_URL + cityNamePattern + "&type=like" + APP_ID_SUFFIX + apiKey);
    }

    String getLatLonData(String lat, String lon){
        return requestData(LAT_LON_BASE_URL +
                "lat=" + Double.parseDouble(lat) +
                "&lon=" + Double.parseDouble(lon) +
                APP_ID_SUFFIX + apiKey);
    }

    Bitmap getImage(String code) {
        Bitmap img = null;
        try {
            img = Picasso.with(context).load(IMG_URL + code + ".png").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }
}
