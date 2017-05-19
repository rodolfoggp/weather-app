package rodolfogusson.weatherapp.model;

import java.util.List;

/**
 * Created by rodolfo on 5/19/17.
 */

public class CityWeather {
    private Location location;
    List<Weather> weatherList;

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void addWeather(Weather weather){
        weatherList.add(weather);
    }
}
