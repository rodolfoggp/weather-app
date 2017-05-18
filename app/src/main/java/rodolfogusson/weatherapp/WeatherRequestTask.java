package rodolfogusson.weatherapp;

import android.os.AsyncTask;

import org.json.JSONException;

import rodolfogusson.weatherapp.model.Weather;

/**
 * Created by rodolfo on 5/17/17.
 */

public class WeatherRequestTask extends AsyncTask<String, Void, Weather>{

    public AsyncResponse callback = null;

    public WeatherRequestTask(AsyncResponse callback){
        this.callback = callback;
    }

    @Override
    protected Weather doInBackground(String... params) {
        Weather weather = new Weather();
        String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));
        try {
            weather = JSONWeatherParser.getWeather(data);
            //Getting the icon:
            //weather.setIcon( (new WeatherHttpClient()).getImage(weather.getCurrentCondition().getIconCode()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather;
    }

    public interface AsyncResponse{
        void processFinish(Weather output);
    }

    @Override
    protected void onPostExecute(Weather weather) {
        callback.processFinish(weather);
    }
}
