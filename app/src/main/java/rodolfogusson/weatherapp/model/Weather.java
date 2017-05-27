package rodolfogusson.weatherapp.model;

import android.util.Log;

import org.joda.time.LocalDate;

/**
 * Created by rodolfo on 5/17/17.
 */

public class Weather {
    private LocalDate date;
    private CurrentCondition currentCondition;
    private Temperature temperature;

    public Weather(){
        currentCondition = new CurrentCondition();
        temperature = new Temperature();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public CurrentCondition getCurrentCondition() {
        return currentCondition;
    }

    public void setCurrentCondition(CurrentCondition currentCondition) {
        this.currentCondition = currentCondition;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public void print(){
        Log.d("DATE: ",date.toString());
        Log.d("CONDITION: ",currentCondition.getCondition());
        Log.d("DESCRIPTION: ",currentCondition.getDescription());
        Log.d("TEMPERATURE: ",temperature.toString());
    }
}
