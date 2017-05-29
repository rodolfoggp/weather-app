package rodolfogusson.weatherapp.communication;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import rodolfogusson.weatherapp.model.CityWeather;

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
        String weatherNowData = ( (new HttpClient((Context) callback)).getWeatherNow(params[0]));
        String weatherForecastData = ( (new HttpClient((Context) callback).getWeatherForecast(params[0], 16)));
        try {
            cityWeather = JSONWeatherParser.getCityWeather(weatherNowData, weatherForecastData);
            //Getting the icon:
            //weather.setIcon( (new HttpClient()).getImage(weather.getCurrentCondition().getIconCode()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cityWeather;
    }

    public interface AsyncResponse{
        void onCityWeatherRetrieved(CityWeather output);
    }

    @Override
    protected void onPostExecute(CityWeather cityWeather) {
        callback.onCityWeatherRetrieved(cityWeather);
    }
}
