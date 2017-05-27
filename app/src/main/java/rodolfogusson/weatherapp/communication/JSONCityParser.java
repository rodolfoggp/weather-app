package rodolfogusson.weatherapp.communication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodolfo on 5/16/17.
 */

class JSONCityParser extends JSONParser{

    static List<String> getCitiesList(String citiesData) throws JSONException{
        List<String> cities = new ArrayList<>();
        if(citiesData != null){
            //Transforming data in JSONObject:
            JSONObject jNow = new JSONObject(citiesData);

            //Getting list of cities structures:
            JSONArray jCitiesArray = jNow.getJSONArray("list");
            for (int i = 0; i < jCitiesArray.length(); i++) {
                JSONObject row = jCitiesArray.getJSONObject(i);
                String city = getString("name",row);
                JSONObject jSys = getObject("sys",row);
                String country = getString("country",jSys);
                String countryAndCity = city + ", " + country;
                if(!cities.contains(countryAndCity)){ //to avoid getting repeated city names on the list
                    cities.add(countryAndCity);
                }
            }
        }
        return cities;
    }
}
