package rodolfogusson.weatherapp.model;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rodolfo on 5/19/17.
 */

public class CityWeather {
    private long id;
    private Location location;
    private List<Weather> weatherList;

    public CityWeather(){
        weatherList = new ArrayList<>();
        location = new Location();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Weather getWeatherAt(int index){
        return weatherList.get(index);
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }
}
