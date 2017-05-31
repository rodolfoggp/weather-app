package rodolfogusson.weatherapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import rodolfogusson.weatherapp.R;

/**
 * Created by rodolfo on 5/27/17.
 */

public class LayoutUtils {

    private static LayoutUtils instance = null;
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

    public String getConvertedTemperatureWithUnit(float kelvinTemp){
        String selectedUnit = spf.getString(context.getString(R.string.key_unit),null);
        if(selectedUnit!=null){
            if(selectedUnit.equals(context.getString(R.string.celsius))){
                return ((int) (kelvinTemp - 273.15)) + context.getString(R.string.celsius_unit);
            }else if(selectedUnit.equals(context.getString(R.string.fahrenheit))){
                return ((int) ((kelvinTemp * 9/5) - 459.67)) + context.getString(R.string.fahrenheit_unit);
            }
        }
        return null;
    }

    public String getConvertedTemperature(float kelvinTemp){
        String selectedUnit = spf.getString(context.getString(R.string.key_unit),null);
        if(selectedUnit!=null){
            if(selectedUnit.equals(context.getString(R.string.celsius))){
                return String.valueOf((int) (kelvinTemp - 273.15));
            }else if(selectedUnit.equals(context.getString(R.string.fahrenheit))){
                return String.valueOf((int) ((kelvinTemp * 9/5) - 459.67));
            }
        }
        return null;
    }

    /*public String getUnitText(){

    }*/

    /*public int getTemperatureNow(String city, String country){

    }*/
}
