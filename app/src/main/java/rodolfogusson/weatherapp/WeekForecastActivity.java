package rodolfogusson.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rodolfogusson.weatherapp.model.CityWeather;
import rodolfogusson.weatherapp.model.Weather;
import rodolfogusson.weatherapp.persistance.DBHelper;

public class WeekForecastActivity extends AppCompatActivity implements WeatherRequestTask.AsyncResponse{

    CityWeather cityWeather;
    TextView city_tv, descr_tv, temp_tv;
    String city, country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_forecast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        city_tv = (TextView) findViewById(R.id.city_text);
        descr_tv = (TextView) findViewById(R.id.description);
        temp_tv = (TextView) findViewById(R.id.tempNow);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<String> cityAndCountry = getCityAndCountryFromPreferences();
        if(cityAndCountry==null){
            //temporary code to have an initial city and country:
            cityAndCountry = new ArrayList<>();
            cityAndCountry.add("Vitoria");
            cityAndCountry.add("BR");
        }
        city = cityAndCountry.get(0);
        country = cityAndCountry.get(1);

        //show weather stored in database for the selected city:
        cityWeather = DBHelper.getInstance(this).findCityWeather(city, country);
        fillData();

        //request updated weather online:
        WeatherRequestTask task = new WeatherRequestTask(this);
        task.execute(city+","+country);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_week_forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillData(){
        if(cityWeather!=null && !cityWeather.getWeatherList().isEmpty()){
            Weather weather = cityWeather.getWeatherAt(0);
            city_tv.setText(cityWeather.getLocation().getCity());
            descr_tv.setText(weather.getCurrentCondition().getDescription());
            temp_tv.setText(String.valueOf(weather.getTemperature().getTempNow()));
        }
    }

    private List<String> getCityAndCountryFromPreferences(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String location = prefs.getString(getString(R.string.key_location),null);
        if(location!=null){
            String[] parts = location.split(", ");
            return new ArrayList<>(Arrays.asList(parts));
        }else{
            return null;
        }
    }

    @Override
    public void processFinish(CityWeather output) {
        cityWeather = output;
        DBHelper helper = DBHelper.getInstance(this);
        helper.save(cityWeather);
        fillData();
    }
}
