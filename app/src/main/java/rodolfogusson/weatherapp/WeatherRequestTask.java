package rodolfogusson.weatherapp;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import rodolfogusson.weatherapp.model.CityWeather;
import rodolfogusson.weatherapp.model.Weather;

/**
 * Created by rodolfo on 5/17/17.
 */

public class WeatherRequestTask extends AsyncTask<String, Void, CityWeather>{

    public AsyncResponse callback = null;

    public WeatherRequestTask(AsyncResponse callback){
        this.callback = callback;
    }

    @Override
    protected CityWeather doInBackground(String... params) {
        CityWeather cityWeather = null;
        String weatherNowData = ( (new WeatherHttpClient((Context) callback)).getWeatherNow(params[0]));
        String weatherForecastData = ( (new WeatherHttpClient((Context) callback).getWeatherForecast(params[0], 16)));
        try {
            cityWeather = JSONWeatherParser.getCityWeather(weatherNowData, weatherForecastData);
            //Getting the icon:
            //weather.setIcon( (new WeatherHttpClient()).getImage(weather.getCurrentCondition().getIconCode()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityWeather;
    }

    public interface AsyncResponse{
        void processFinish(CityWeather output);
    }

    @Override
    protected void onPostExecute(CityWeather cityWeather) {
        callback.processFinish(cityWeather);
    }
}
