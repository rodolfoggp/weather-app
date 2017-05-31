package rodolfogusson.weatherapp.communication;

import org.joda.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rodolfogusson.weatherapp.model.CityWeather;
import rodolfogusson.weatherapp.model.CurrentCondition;
import rodolfogusson.weatherapp.model.Location;
import rodolfogusson.weatherapp.model.Temperature;
import rodolfogusson.weatherapp.model.Weather;

/**
 * Created by rodolfo on 5/16/17.
 */

public class JSONWeatherParser extends JSONParser{

    public static CityWeather getCityWeather(String weatherNowData, String weatherForecastData) throws JSONException{
        CityWeather cityWeather = null;
        if(weatherNowData != null && weatherForecastData != null){
            cityWeather = new CityWeather();
            //Getting the weather info for now:
            //Transforming data in JSONObject:
            JSONObject jNow = new JSONObject(weatherNowData);

            //getting location:
            JSONObject jCoord = getObject("coord", jNow);
            Location location = new Location();
            location.setLatitude(getFloat("lat",jCoord));
            location.setLongitude(getFloat("lon",jCoord));
            JSONObject jSys = getObject("sys",jNow);
            location.setCountry(getString("country",jSys));
            location.setCity(getString("name",jNow));
            cityWeather.setLocation(location);

            //getting weather for today:
            JSONArray jWeatherArray = jNow.getJSONArray("weather");
            JSONObject jWeatherToday = jWeatherArray.getJSONObject(0);
            Weather weatherToday = new Weather();
            long dt = jNow.getLong("dt");
            LocalDate dateToday = new LocalDate(dt*1000); //dt*1000 to transform dt(in seconds) to millis
            weatherToday.setDate(dateToday);
            weatherToday.getCurrentCondition().setWeatherId(getInt("id",jWeatherToday));
            weatherToday.getCurrentCondition().setCondition(getString("main",jWeatherToday));
            weatherToday.getCurrentCondition().setDescription(getString("description",jWeatherToday));
            weatherToday.getCurrentCondition().setIconCode(getString("icon",jWeatherToday));

            //getting temperature, pressure and humidity for today:
            JSONObject jMain = getObject("main", jNow);
            weatherToday.getTemperature().setTempNow(getFloat("temp",jMain));
            weatherToday.getCurrentCondition().setPressure(getFloat("pressure",jMain));
            weatherToday.getCurrentCondition().setHumidity(getInt("humidity",jMain));

            //setting the weather built as the first weather of cityWeather (weather today):
            cityWeather.addWeather(weatherToday);

            //Getting the weather forecast for 16 days:
            //Transforming data in JSONObject:
            JSONObject jForecast = new JSONObject(weatherForecastData);

            //Getting the list of weather measurements:
            JSONArray jList = jForecast.getJSONArray("list");

            /* max and min temperatures for today are taken from the first element of
             * the list in weatherForecastData:
             */
            JSONObject jTempToday = jList.getJSONObject(0).getJSONObject("temp");
            weatherToday.getTemperature().setMinTemp(getFloat("min",jTempToday));
            weatherToday.getTemperature().setMaxTemp(getFloat("max",jTempToday));

            //Starting at i = 1, to get the forecast for the next day and on:
            for (int i = 1; i < jList.length(); i++) {
                JSONObject row = jList.getJSONObject(i);
                Weather weather = new Weather();

                //getting date for this weather:
                LocalDate date = new LocalDate(dateToday);
                date.plusDays(i);
                weather.setDate(date);

                //getting temperature:
                JSONObject jTemp = row.getJSONObject("temp");
                Temperature temperature = weather.getTemperature();
                temperature.setTempNow(null); //We dont know this temp yet, its in the future
                temperature.setDay(getFloat("day",jTemp));
                temperature.setMinTemp(getFloat("min",jTemp));
                temperature.setMaxTemp(getFloat("max",jTemp));
                temperature.setNight(getFloat("night",jTemp));
                temperature.setEvening(getFloat("eve",jTemp));
                temperature.setMorning(getFloat("morn",jTemp));

                //getting pressure:
                CurrentCondition currentCondition = weather.getCurrentCondition();
                currentCondition.setPressure(getFloat("pressure",row));

                //getting humidity:
                currentCondition.setHumidity(getInt("humidity",row));

                //getting weather description:
                JSONArray jWeatherForecastArray = row.getJSONArray("weather");
                JSONObject jWeather = jWeatherForecastArray.getJSONObject(0);
                currentCondition.setCondition(getString("main",jWeather));
                currentCondition.setDescription(getString("description",jWeather));
                currentCondition.setWeatherId(getInt("id",jWeather));
                currentCondition.setIconCode(getString("icon",jWeatherToday));

                cityWeather.addWeather(weather);
            }
        }
        return cityWeather;
    }
}
