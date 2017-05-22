package rodolfogusson.weatherapp.persistance;

import android.database.Cursor;

import rodolfogusson.weatherapp.model.CityWeather;

import static android.provider.BaseColumns._ID;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.CityWeatherColumns.CITY;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.CityWeatherColumns.COUNTRY;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.CityWeatherColumns.LATITUDE;
import static rodolfogusson.weatherapp.persistance.CityWeatherContract.CityWeatherColumns.LONGITUDE;

/**
 * Created by rodolfo on 5/20/17.
 */

public class CityWeatherDeserializer {

    public CityWeather deserialize(Cursor cursor, DBHelper helper){
        CityWeather cityWeather = new CityWeather();

        cityWeather.setId(readID(cursor));
        cityWeather.getLocation().setCity(readCity(cursor));
        cityWeather.getLocation().setCountry(readCountry(cursor));
        cityWeather.getLocation().setLatitude(readLatitude(cursor));
        cityWeather.getLocation().setLongitude(readLongitude(cursor));
        cityWeather.setWeatherList(helper.findWeathersFor(readID(cursor)));

        return cityWeather;
    }

    private long readID(Cursor cursor){
        int i = cursor.getColumnIndex(_ID);
        return i != -1 ? cursor.getLong(i) : -1;
    }

    private float readLatitude(Cursor cursor){
        int i = cursor.getColumnIndex(LATITUDE);
        return cursor.getFloat(i);
    }

    private float readLongitude(Cursor cursor){
        int i = cursor.getColumnIndex(LONGITUDE);
        return cursor.getFloat(i);
    }

    private String readCountry(Cursor cursor){
        int i = cursor.getColumnIndex(COUNTRY);
        return cursor.getString(i);
    }

    private String readCity(Cursor cursor){
        int i = cursor.getColumnIndex(CITY);
        return cursor.getString(i);
    }

}
