package rodolfogusson.weatherapp.persistance;

import android.content.ContentValues;

import rodolfogusson.weatherapp.model.Weather;

import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.CONDITION;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.DATE;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.DAY;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.DESCRIPTION;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.EVENING;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.HUMIDITY;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.LOCATION_ID;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.MAX_TEMP;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.MIN_TEMP;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.MORNING;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.NIGHT;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.PRESSURE;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.TEMP_NOW;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.WeatherColumns.WEATHER_ID;


/**
 * Created by rodolfo on 5/19/17.
 */

public class WeatherSerializer {

    public static ContentValues serialize(Weather weather, long foreignKey){
        ContentValues values = new ContentValues();
        values.put(LOCATION_ID,foreignKey);
        values.put(DATE,weather.getDate().toString());
        values.put(WEATHER_ID,weather.getCurrentCondition().getWeatherId());
        values.put(HUMIDITY,weather.getCurrentCondition().getHumidity());
        values.put(PRESSURE,weather.getCurrentCondition().getPressure());
        values.put(DESCRIPTION,weather.getCurrentCondition().getDescription());
        values.put(CONDITION,weather.getCurrentCondition().getCondition());
        values.put(TEMP_NOW,weather.getTemperature().getTempNow());
        values.put(MIN_TEMP,weather.getTemperature().getMinTemp());
        values.put(MAX_TEMP,weather.getTemperature().getMaxTemp());
        values.put(MORNING,weather.getTemperature().getMorning());
        values.put(DAY,weather.getTemperature().getDay());
        values.put(EVENING,weather.getTemperature().getEvening());
        values.put(NIGHT,weather.getTemperature().getNight());
        return values;
    }
}
