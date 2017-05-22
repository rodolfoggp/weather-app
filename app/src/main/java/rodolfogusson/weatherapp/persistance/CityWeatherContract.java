package rodolfogusson.weatherapp.persistance;

import android.provider.BaseColumns;

/**
 * Created by rodolfo on 5/20/17.
 */

public class CityWeatherContract {
    public static final String CITIES_TABLE = "cities";
    public static final String WEATHERS_TABLE = "weathers";

    //Column names for CityWeathers:
    public abstract class CityWeatherColumns implements BaseColumns{
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String COUNTRY = "country";
        public static final String CITY = "city";
    }

    //Column names for Weathers:
    public abstract class WeatherColumns implements BaseColumns{
        public static final String LOCATION_ID = "location_id";
        public static final String DATE = "date";
        public static final String WEATHER_ID = "weather_id";
        public static final String HUMIDITY = "humidity";
        public static final String PRESSURE = "pressure";
        public static final String DESCRIPTION = "description";
        public static final String CONDITION = "condition";
        public static final String TEMP_NOW = "temp_now";
        public static final String MIN_TEMP = "min_temp";
        public static final String MAX_TEMP = "max_temp";
        public static final String MORNING = "morning";
        public static final String DAY = "day";
        public static final String EVENING = "evening";
        public static final String NIGHT = "night";
    }

    //prevents instantiation of this class:
    private CityWeatherContract(){}
}
