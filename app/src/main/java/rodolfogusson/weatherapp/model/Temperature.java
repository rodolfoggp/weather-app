package rodolfogusson.weatherapp.model;

/**
 * Created by rodolfo on 5/16/17.
 */

public class Temperature {
    float tempNow, minTemp, maxTemp, morning, day, evening, night;

    public float getTempNow() {
        return tempNow;
    }

    public void setTempNow(float tempNow) {
        this.tempNow = tempNow;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public float getMorning() {
        return morning;
    }

    public void setMorning(float morning) {
        this.morning = morning;
    }

    public float getDay() {
        return day;
    }

    public void setDay(float day) {
        this.day = day;
    }

    public float getEvening() {
        return evening;
    }

    public void setEvening(float evening) {
        this.evening = evening;
    }

    public float getNight() {
        return night;
    }

    public void setNight(float night) {
        this.night = night;
    }
}
