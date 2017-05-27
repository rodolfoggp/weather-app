package rodolfogusson.weatherapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rodolfogusson.weatherapp.communication.WeatherRequestTask;
import rodolfogusson.weatherapp.model.CityWeather;
import rodolfogusson.weatherapp.model.Weather;
import rodolfogusson.weatherapp.persistance.DBHelper;

public class WeekForecastActivity extends AppCompatActivity implements WeatherRequestTask.AsyncResponse{

    CityWeather cityWeather;
    TextView city_tv, descr_tv, temp_tv;
    String city, country;
    SharedPreferences prefs;
    boolean isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_forecast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        city_tv = (TextView) findViewById(R.id.city_text);
        descr_tv = (TextView) findViewById(R.id.description);
        temp_tv = (TextView) findViewById(R.id.tempNow);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isFirstRun = prefs.getBoolean(getString(R.string.key_is_first_run), true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        verifyApiKey();

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
            LayoutUtils utils = LayoutUtils.getInstance().init(this);
            Integer temp = utils.getConvertedTemperature(weather.getTemperature().getTempNow());
            temp_tv.setText(String.valueOf(temp));
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
        if(cityWeather!=null){
            DBHelper helper = DBHelper.getInstance(this);
            /*//get rid of old data:
            helper.deleteAllFor(cityWeather);*/
            //save the new retrieved data:
            helper.save(cityWeather);
            fillData();
        }else{
            Snackbar.make(findViewById(R.id.coordinator_layout),
                    getString(R.string.error_getting_data),
                    Snackbar.LENGTH_LONG).show();
        }
    }

    private void verifyApiKey(){
        String apiKey = prefs.getString(getString(R.string.key_api_key),null);
        if(apiKey == null || apiKey.isEmpty() && isFirstRun){
            isFirstRun = false;
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("API Key not set")
                    .setMessage("This app needs a valid Openweather API Key to work, please set a valid key in the Settings screen")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            prefs.edit().putBoolean(getString(R.string.key_is_first_run), isFirstRun).apply();
        }
    }
}
