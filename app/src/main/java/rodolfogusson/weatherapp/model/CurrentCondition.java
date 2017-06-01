package rodolfogusson.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rodolfo on 5/16/17.
 */

public class CurrentCondition implements Parcelable {
    private int weatherId;
    private int humidity;
    private float pressure;
    private String description;
    private String condition;
    private String iconCode;

    public CurrentCondition(){

    }

    public String getIconCode() {
        return iconCode;
    }

    public void setIconCode(String iconCode) {
        this.iconCode = iconCode;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public int getWeatherId() {
        return weatherId;

    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    private CurrentCondition(Parcel in) {
        weatherId = in.readInt();
        humidity = in.readInt();
        pressure = in.readFloat();
        description = in.readString();
        condition = in.readString();
        iconCode = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(weatherId);
        dest.writeInt(humidity);
        dest.writeFloat(pressure);
        dest.writeString(description);
        dest.writeString(condition);
        dest.writeString(iconCode);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CurrentCondition> CREATOR = new Parcelable.Creator<CurrentCondition>() {
        @Override
        public CurrentCondition createFromParcel(Parcel in) {
            return new CurrentCondition(in);
        }

        @Override
        public CurrentCondition[] newArray(int size) {
            return new CurrentCondition[size];
        }
    };
}