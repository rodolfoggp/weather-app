package rodolfogusson.weatherapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

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
        //TODO: get from settings
        city = "Trevi";
        country = "IT";

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
            //TODO: Remove
            DBHelper.getInstance(this).cleanDB();
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

    @Override
    public void processFinish(CityWeather output) {
        cityWeather = output;
        DBHelper helper = DBHelper.getInstance(this);
        helper.save(cityWeather);
        fillData();
    }
}
