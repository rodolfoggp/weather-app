package rodolfogusson.weatherapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import rodolfogusson.weatherapp.communication.CityRequestTask;
import rodolfogusson.weatherapp.communication.WeatherRequestTask;
import rodolfogusson.weatherapp.model.CityWeather;
import rodolfogusson.weatherapp.model.Weather;
import rodolfogusson.weatherapp.persistance.DBHelper;

public class WeekForecastActivity extends AppCompatActivity implements WeatherRequestTask.AsyncResponse, CityRequestTask.AsyncResponse{

    CityWeather cityWeather;
    TextView city_tv, descr_tv, temp_tv;
    SharedPreferences prefs;
    boolean isFirstRun;
    boolean gettingResultsFromActivity = false;

    private double currentLatitude;
    private double currentLongitude;

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
        if(!gettingResultsFromActivity){
            getAndDisplayData();
        }else{
            gettingResultsFromActivity = false;
        }
        /*
        if(!AppUtils.isNetworkAvailable(this)){
            //Show warning: no internet!
        }*/
    }

    private void getAndDisplayData(){
        if(isAPIKeySet()){
            String location = prefs.getString(getString(R.string.key_location),getString(R.string.current_location));
            String currentLocation = getString(R.string.current_location);
            String cityAndCountry;
            if(location.equals(currentLocation)){
                //Using current location of the device:
                //get the last city used by the app:
                cityAndCountry = prefs.getString(getString(R.string.key_last_location),null);
                //show weather stored in database for the selected city:
                showWeatherDataInDBFor(cityAndCountry);
                //get current latitude and longitude and get weather data for them:
                gettingResultsFromActivity = true;
                Intent intent = new Intent(this, GPSTrackerActivity.class);
                startActivityForResult(intent,1);
            }else{
                //Using city set in preferences:
                cityAndCountry = prefs.getString(getString(R.string.key_location),null);
                //show weather stored in database for the selected city:
                showWeatherDataInDBFor(cityAndCountry);
                //request updated weather online:
                WeatherRequestTask task = new WeatherRequestTask(this);
                task.execute(cityAndCountry);
            }
        }
    }

    private void showWeatherDataInDBFor(String cityAndCountry){
        if(cityAndCountry != null){
            cityWeather = DBHelper.getInstance(this).findCityWeather(cityAndCountry);
            fillData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_week_forecast, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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

    private boolean isAPIKeySet(){
        String apiKey = prefs.getString(getString(R.string.key_api_key),null);
        if(apiKey == null || apiKey.isEmpty() && isFirstRun){
            isFirstRun = false;
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            builder.setTitle("API Key not set")
                    .setMessage("This app needs a valid Openweather API Key to work, please set a valid key in the Settings screen")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            prefs.edit().putBoolean(getString(R.string.key_is_first_run), isFirstRun).apply();
            return false;
        }else{
            return true;
        }
    }

    /**
     * When we get latitude and longitude from gps:
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            Bundle extras = data.getExtras();
            currentLongitude = extras.getDouble("Longitude");
            currentLatitude = extras.getDouble("Latitude");
            //fetch the city that has these coordinates:
            CityRequestTask task = new CityRequestTask(this);
            task.execute(String.valueOf(currentLatitude),String.valueOf(currentLongitude));
        }
    }

    /**
     * When we get the city with given coordinates:
     */
    @Override
    public void onCityRetrieved(List<String> output) {
        //This list will only have one city:
        String cityAndCountry = output.get(0);
        //Save it as the last city used by the app:
        prefs.edit().putString(getString(R.string.key_last_location),
                cityAndCountry).apply();
        //request updated weather online:
        WeatherRequestTask task = new WeatherRequestTask(this);
        task.execute(cityAndCountry);
    }

    @Override
    public void onCityWeatherRetrieved(CityWeather output) {
        cityWeather = output;
        if(cityWeather!=null){
            DBHelper helper = DBHelper.getInstance(this);
            //save the new retrieved data:
            helper.save(cityWeather);
            fillData();
        }else{
            Snackbar.make(findViewById(R.id.coordinator_layout),
                    getString(R.string.error_getting_data),
                    Snackbar.LENGTH_LONG).show();
        }
    }
}
