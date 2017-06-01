package rodolfogusson.weatherapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rodolfo on 5/16/17.
 */

public class Temperature implements Parcelable {
    private Float tempNow;
    private float minTemp;
    private float maxTemp;
    private float morning;
    private float day;
    private float evening;
    private float night;

    public Temperature(){

    }

    public Float getTempNow() {
        return tempNow;
    }

    public void setTempNow(Float tempNow) {
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

    Temperature(Parcel in) {
        tempNow = in.readByte() == 0x00 ? null : in.readFloat();
        minTemp = in.readFloat();
        maxTemp = in.readFloat();
        morning = in.readFloat();
        day = in.readFloat();
        evening = in.readFloat();
        night = in.readFloat();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (tempNow == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeFloat(tempNow);
        }
        dest.writeFloat(minTemp);
        dest.writeFloat(maxTemp);
        dest.writeFloat(morning);
        dest.writeFloat(day);
        dest.writeFloat(evening);
        dest.writeFloat(night);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Temperature> CREATOR = new Parcelable.Creator<Temperature>() {
        @Override
        public Temperature createFromParcel(Parcel in) {
            return new Temperature(in);
        }

        @Override
        public Temperature[] newArray(int size) {
            return new Temperature[size];
        }
    };
}