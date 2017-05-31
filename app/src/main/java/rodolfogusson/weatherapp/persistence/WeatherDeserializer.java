package rodolfogusson.weatherapp.persistence;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.joda.time.LocalDate;

import rodolfogusson.weatherapp.model.Weather;

import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.CONDITION;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.DATE;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.DAY;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.DESCRIPTION;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.EVENING;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.HUMIDITY;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.ICON;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.MAX_TEMP;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.MIN_TEMP;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.MORNING;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.NIGHT;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.PRESSURE;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.TEMP_NOW;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.WeatherColumns.WEATHER_ID;

/**
 * Created by rodolfo on 5/20/17.
 */

public class WeatherDeserializer {

    public Weather deserialize(Cursor cursor){
        Weather weather = new Weather();

        String dateString = readDate(cursor);
        weather.setDate(new LocalDate(dateString));
        weather.getCurrentCondition().setWeatherId(readWeatherID(cursor));
        weather.getCurrentCondition().setHumidity(readHumidity(cursor));
        weather.getCurrentCondition().setPressure(readPressure(cursor));
        weather.getCurrentCondition().setDescription(readDescription(cursor));
        weather.getCurrentCondition().setCondition(readCondition(cursor));
        weather.getTemperature().setTempNow(readTempNow(cursor));
        weather.getTemperature().setMinTemp(readMinTemp(cursor));
        weather.getTemperature().setMaxTemp(readMaxTemp(cursor));
        weather.getTemperature().setMorning(readMorning(cursor));
        weather.getTemperature().setDay(readDay(cursor));
        weather.getTemperature().setEvening(readEvening(cursor));
        weather.getTemperature().setNight(readNight(cursor));
        weather.setIcon(readIcon(cursor));
        return weather;
    }

    private String readDate(Cursor cursor){
        int i = cursor.getColumnIndex(DATE);
        return cursor.getString(i);
    }

    private int readWeatherID(Cursor cursor){
        int i = cursor.getColumnIndex(WEATHER_ID);
        return cursor.getInt(i);
    }

    private int readHumidity(Cursor cursor){
        int i = cursor.getColumnIndex(HUMIDITY);
        return cursor.getInt(i);
    }

    private float readPressure(Cursor cursor){
        int i = cursor.getColumnIndex(PRESSURE);
        return cursor.getFloat(i);
    }

    private String readDescription(Cursor cursor){
        int i = cursor.getColumnIndex(DESCRIPTION);
        return cursor.getString(i);
    }

    private String readCondition(Cursor cursor){
        int i = cursor.getColumnIndex(CONDITION);
        return cursor.getString(i);
    }

    private Float readTempNow(Cursor cursor){
        int i = cursor.getColumnIndex(TEMP_NOW);
        return cursor.getFloat(i);
    }

    private float readMinTemp(Cursor cursor){
        int i = cursor.getColumnIndex(MIN_TEMP);
        return cursor.getFloat(i);
    }

    private float readMaxTemp(Cursor cursor){
        int i = cursor.getColumnIndex(MAX_TEMP);
        return cursor.getFloat(i);
    }

    private float readMorning(Cursor cursor){
        int i = cursor.getColumnIndex(MORNING);
        return cursor.getFloat(i);
    }

    private float readDay(Cursor cursor){
        int i = cursor.getColumnIndex(DAY);
        return cursor.getFloat(i);
    }

    private float readEvening(Cursor cursor){
        int i = cursor.getColumnIndex(EVENING);
        return cursor.getFloat(i);
    }

    private float readNight(Cursor cursor){
        int i = cursor.getColumnIndex(NIGHT);
        return cursor.getFloat(i);
    }

    private Bitmap readIcon(Cursor cursor){
        int i = cursor.getColumnIndex(ICON);
        byte[] image = cursor.getBlob(i);
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
