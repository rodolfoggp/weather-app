package rodolfogusson.weatherapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rodolfogusson.weatherapp.model.Location;
import rodolfogusson.weatherapp.model.Weather;

/**
 * Created by rodolfo on 5/16/17.
 */

public class JSONWeatherParser {

    public static Weather getWeather(String data) throws JSONException{
        Weather weather = new Weather();

        //Transforming data in JSONObject:
        JSONObject jObj = new JSONObject(data);

        //Getting the current condition:
        JSONArray jWeatherArray = jObj.getJSONArray("weather");
        JSONObject jWeather = jWeatherArray.getJSONObject(0);
        weather.getCurrentCondition().setWeatherId(getInt("id", jWeather));
        weather.getCurrentCondition().setDescription(getString("description", jWeather));
        weather.getCurrentCondition().setCondition(getString("main", jWeather));
        weather.getCurrentCondition().setIconCode(getString("icon", jWeather));

        //Getting the data from main object:
        JSONObject jMain = getObject("main", jObj);
        weather.getCurrentCondition().setHumidity(getInt("humidity", jMain));
        weather.getCurrentCondition().setPressure(getInt("pressure", jMain));
        weather.getTemperature().setMaxTemp(getFloat("temp_max", jMain));
        weather.getTemperature().setMinTemp(getFloat("temp_min", jMain));
        weather.getTemperature().setTemp(getFloat("temp", jMain));

        //Getting the location:
        Location location = new Location();
        JSONObject jCoordinates = getObject("coord", jObj);
        location.setLatitude(getFloat("lat", jCoordinates));
        location.setLongitude(getFloat("lon", jCoordinates));
        JSONObject jSys = getObject("sys", jObj);
        location.setCountry(getString("country", jSys));
        location.setCity(getString("name", jObj));
        weather.setLocation(location);

        return weather;
    }

    private static JSONObject getObject(String tagName, JSONObject jObj) throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

}
