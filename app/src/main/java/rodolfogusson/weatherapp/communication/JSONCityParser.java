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

            if(jNow.has("list")){
                /*
                * This means that we are looking for cities using a city name pattern.
                * The json that openweather answers back has a "list" tag in that case.
                * */
                //Getting list of cities structures:
                JSONArray jCitiesArray = jNow.getJSONArray("list");
                for (int i = 0; i < jCitiesArray.length(); i++) {
                    JSONObject row = jCitiesArray.getJSONObject(i);
                    String cityAndCountry = getCityAndCountry(row);
                    if(!cities.contains(cityAndCountry)){ //to avoid getting repeated city names on the list
                        cities.add(cityAndCountry);
                    }
                }
            }else{
                /*
                * In this case, we are looking for a single city, using coordinates.
                * The structure of the json is a little different than the previous case.
                * */
                String cityAndCountry = getCityAndCountry(jNow);
                cities.add(cityAndCountry);
            }
        }
        return cities;
    }

    static private String getCityAndCountry(JSONObject obj) throws JSONException{
        String city = getString("name",obj);
        JSONObject jSys = getObject("sys",obj);
        String country = getString("country",jSys);
        return city + ", " + country;
    }
}
