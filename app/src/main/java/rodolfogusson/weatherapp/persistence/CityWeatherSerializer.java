package rodolfogusson.weatherapp.persistence;

import android.content.ContentValues;

import rodolfogusson.weatherapp.model.CityWeather;

import static rodolfogusson.weatherapp.persistence.CityWeatherContract.CityWeatherColumns.CITY;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.CityWeatherColumns.COUNTRY;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.CityWeatherColumns.LATITUDE;
import static rodolfogusson.weatherapp.persistence.CityWeatherContract.CityWeatherColumns.LONGITUDE;


/**
 * Created by rodolfo on 5/19/17.
 */

class CityWeatherSerializer {

    static ContentValues serialize(CityWeather cityWeather) {
        ContentValues values = new ContentValues();
        values.put(LATITUDE, cityWeather.getLocation().getLatitude());
        values.put(LONGITUDE, cityWeather.getLocation().getLongitude());
        values.put(COUNTRY, cityWeather.getLocation().getCountry());
        values.put(CITY, cityWeather.getLocation().getCity());
        return values;
    }
}
