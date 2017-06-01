package rodolfogusson.weatherapp.persistence;

import android.provider.BaseColumns;

/**
 * Created by rodolfo on 5/20/17.
 */

class CityWeatherContract {
    static final String CITIES_TABLE = "cities";
    static final String WEATHERS_TABLE = "weathers";

    //Column names for CityWeathers:
    abstract class CityWeatherColumns implements BaseColumns{
        static final String LATITUDE = "latitude";
        static final String LONGITUDE = "longitude";
        static final String COUNTRY = "country";
        static final String CITY = "city";
    }

    //Column names for Weathers:
    abstract class WeatherColumns implements BaseColumns{
        static final String LOCATION_ID = "location_id";
        static final String DATE = "date";
        static final String WEATHER_ID = "weather_id";
        static final String HUMIDITY = "humidity";
        static final String PRESSURE = "pressure";
        static final String DESCRIPTION = "description";
        static final String CONDITION = "condition";
        static final String ICON_CODE = "icon_code";
        static final String TEMP_NOW = "temp_now";
        static final String MIN_TEMP = "min_temp";
        static final String MAX_TEMP = "max_temp";
        static final String MORNING = "morning";
        static final String DAY = "day";
        static final String EVENING = "evening";
        static final String NIGHT = "night";
        static final String ICON = "icon";
    }

    //prevents instantiation of this class:
    private CityWeatherContract(){}
}
