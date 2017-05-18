package rodolfogusson.weatherapp.model;

/**
 * Created by rodolfo on 5/17/17.
 */

public class Weather {
    private CurrentCondition currentCondition;
    private Location location;
    private Temperature temperature;

    public Weather(){
        currentCondition = new CurrentCondition();
        location = new Location();
        temperature = new Temperature();
    }

    public CurrentCondition getCurrentCondition() {
        return currentCondition;
    }

    public void setCurrentCondition(CurrentCondition currentCondition) {
        this.currentCondition = currentCondition;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }
}
