package rodolfogusson.weatherapp.persistence;

import android.content.ContentValues;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import rodolfogusson.weatherapp.model.Weather;

import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.CONDITION;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.DATE;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.DAY;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.DESCRIPTION;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.EVENING;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.HUMIDITY;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.ICON;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.ICON_CODE;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.LOCATION_ID;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.MAX_TEMP;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.MIN_TEMP;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.MORNING;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.NIGHT;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.PRESSURE;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.TEMP_NOW;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.WEATHER_ID;


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
        values.put(ICON_CODE,weather.getCurrentCondition().getIconCode());
        values.put(TEMP_NOW,weather.getTemperature().getTempNow());
        values.put(MIN_TEMP,weather.getTemperature().getMinTemp());
        values.put(MAX_TEMP,weather.getTemperature().getMaxTemp());
        values.put(MORNING,weather.getTemperature().getMorning());
        values.put(DAY,weather.getTemperature().getDay());
        values.put(EVENING,weather.getTemperature().getEvening());
        values.put(NIGHT,weather.getTemperature().getNight());

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        weather.getIcon().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        values.put(ICON,byteArray);
        return values;
    }
}
