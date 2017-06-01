package rodolfogusson.weatherapp.communication;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import org.json.JSONException;

import rodolfogusson.weatherapp.model.CityWeather;
import rodolfogusson.weatherapp.model.Weather;

/**
 * Created by rodolfo on 5/17/17.
 */

public class WeatherRequestTask extends AsyncTask<String, Void, CityWeather>{

    private AsyncResponse callback = null;

    public WeatherRequestTask(AsyncResponse callback){
        this.callback = callback;
    }

    @Override
    protected CityWeather doInBackground(String... params) {
        CityWeather cityWeather = null;
        String weatherNowData = ( (new HttpClient((Context) callback)).getWeatherNow(params[0]));
        String weatherForecastData = ( (new HttpClient((Context) callback).getWeatherForecast(params[0])));
        try {
            cityWeather = JSONWeatherParser.getCityWeather(weatherNowData, weatherForecastData);
            if(cityWeather != null){
                //Getting the icons:
                for(Weather weather : cityWeather.getWeatherList()){
                    Bitmap icon = (new HttpClient((Context) callback)).getImage(weather.getCurrentCondition().getIconCode());
                    weather.setIcon(icon);
                }
            }
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
