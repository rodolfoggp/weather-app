package rodolfogusson.weatherapp.communication;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodolfo on 5/17/17.
 */

public class CityRequestTask extends AsyncTask<String, Void, List<String>>{

    private AsyncResponse callback = null;

    public CityRequestTask(AsyncResponse callback){
        this.callback = callback;
    }

    @Override
    protected List<String> doInBackground(String... params) {
        List<String> cities = new ArrayList<>();
        String citiesData;
        if(params.length > 1){
            //search for latitude and longitude
            citiesData = ( (new HttpClient((Context) callback)).getLatLonData(params[0], params[1]));
        }else{
            //search for city name pattern
            citiesData = ( (new HttpClient((Context) callback)).getCitiesData(params[0]));
        }
        try {
            cities = JSONCityParser.getCitiesList(citiesData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cities;
    }

    public interface AsyncResponse{
        void onCityRetrieved(List<String> output);
    }

    @Override
    protected void onPostExecute(List<String> cities) {
        callback.onCityRetrieved(cities);
    }
}
