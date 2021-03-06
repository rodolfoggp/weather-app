package rodolfogusson.weatherapp.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Created by rodolfo on 5/17/17.
 */

public class Weather implements Parcelable {
    private LocalDate date;
    private CurrentCondition currentCondition;
    private Temperature temperature;
    private Bitmap icon;

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

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    /**
     * This method will be used to show the current temperature for the selected city,
     * on the WeekForecastActivity. If the temperature.getTempNow() is set, that means
     * we have fetched the temperature for this location today, and, being it up-to-date
     * or not (we may be disconnected from the internet when calling this method), that
     * is the temperature that will be shown.
     * Otherwise, if temperature.getTempNow() is not set, that means we haven't fetched
     * the temperature for this location today. In that case, we will assume the
     * temperature now will be what the forecast predicted, depending on the hour of the
     * day (morning, day, evening or night).
     */
    public Float getTemperatureNow(){
        Float tempNow = temperature.getTempNow();
        if(tempNow != null && tempNow != 0.0){ //0.0 Kelvin, means this variable has not been set
            //The temperature for this day has been set already:
            return tempNow;
        }else{
            //The temperature for this day has not been set:
            LocalTime lt = new LocalTime();
            int hour = lt.getHourOfDay();
            if(hour >= 6 && hour <= 11){
                //morning:
                return temperature.getMorning();
            }else if(hour >= 12 && hour <= 17){
                //day:
                return temperature.getDay();
            }else if(hour >= 18 && hour <= 23){
                //evening:
                return temperature.getEvening();
            }else if(hour >= 0 && hour <= 5){
                //night:
                return temperature.getNight();
            }
        }
        return null;
    }

    private Weather(Parcel in) {
        date = (LocalDate) in.readValue(LocalDate.class.getClassLoader());
        currentCondition = (CurrentCondition) in.readValue(CurrentCondition.class.getClassLoader());
        temperature = (Temperature) in.readValue(Temperature.class.getClassLoader());
        icon = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(date);
        dest.writeValue(currentCondition);
        dest.writeValue(temperature);
        dest.writeValue(icon);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Weather> CREATOR = new Parcelable.Creator<Weather>() {
        @Override
        public Weather createFromParcel(Parcel in) {
            return new Weather(in);
        }

        @Override
        public Weather[] newArray(int size) {
            return new Weather[size];
        }
    };
}