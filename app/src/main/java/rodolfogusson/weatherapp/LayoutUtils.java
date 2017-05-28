package rodolfogusson.weatherapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by rodolfo on 5/27/17.
 */

public class LayoutUtils {

    private static LayoutUtils instance = null;

    private static String selectedUnit;
    private static SharedPreferences spf;
    private Context context;

    private LayoutUtils(){}

    public static LayoutUtils getInstance(){
        if(instance == null) {
            instance = new LayoutUtils();
        }
        return instance;
    }

    public LayoutUtils init(Context context){
        this.context = context;
        spf = PreferenceManager.getDefaultSharedPreferences(context);
        return this;
    }

    public Integer getConvertedTemperature(float kelvinTemp){
        String selectedUnit = spf.getString(context.getString(R.string.key_unit),null);
        if(selectedUnit!=null){
            if(selectedUnit.equals(context.getString(R.string.celsius))){
                return (int) (kelvinTemp - 273.15);
            }else if(selectedUnit.equals(context.getString(R.string.fahrenheit))){
                return (int) ((kelvinTemp * 9/5) - 459.67);
            }
        }
        return null;
    }

    /*public int getTemperatureNow(String city, String country){

    }*/
}
