package rodolfogusson.weatherapp.forecast;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;
import java.util.Locale;

import rodolfogusson.weatherapp.R;
import rodolfogusson.weatherapp.communication.CityRequestTask;
import rodolfogusson.weatherapp.communication.WeatherRequestTask;
import rodolfogusson.weatherapp.model.CityWeather;
import rodolfogusson.weatherapp.model.Weather;
import rodolfogusson.weatherapp.persistence.DBHelper;
import rodolfogusson.weatherapp.settings.SettingsActivity;
import rodolfogusson.weatherapp.utilities.AppUtils;
import rodolfogusson.weatherapp.utilities.GPSTracker;
import rodolfogusson.weatherapp.utilities.LayoutUtils;

public class WeekForecastActivity extends AppCompatActivity implements WeatherRequestTask.AsyncResponse, CityRequestTask.AsyncResponse{

    private CityWeather cityWeather;
    private TextView city_tv, day_tv, descr_tv, temp_tv, temp_high_tv, temp_low_tv;
    private ImageView weather_today_img;

    private RecyclerView forecastRecyclerView;
    private ForecastAdapter adapter;

    private SharedPreferences prefs;

    private boolean isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_forecast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        city_tv = (TextView) findViewById(R.id.city);
        day_tv = (TextView) findViewById(R.id.day_of_week);
        descr_tv = (TextView) findViewById(R.id.description);
        temp_tv = (TextView) findViewById(R.id.temp_now);
        temp_high_tv = (TextView) findViewById(R.id.temp_high);
        temp_low_tv = (TextView) findViewById(R.id.temp_low);
        weather_today_img = (ImageView) findViewById(R.id.weather_today_img);

        forecastRecyclerView = (RecyclerView) findViewById(R.id.forecast_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        forecastRecyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(forecastRecyclerView.getContext(),
                linearLayoutManager.getOrientation());
        forecastRecyclerView.addItemDecoration(dividerItemDecoration);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        isFirstRun = prefs.getBoolean(getString(R.string.key_is_first_run), true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //ensures that we are only working with up-to-date weather data:
        DBHelper.getInstance(this).removeExpiredWeathers();
        getAndDisplayData();
        if(!AppUtils.isNetworkAvailable(this)){
            //Show warning: no internet!
            final Snackbar snack = Snackbar.make(findViewById(R.id.coordinator_layout),
                    getString(R.string.error_no_internet),
                    Snackbar.LENGTH_INDEFINITE);
            snack.setActionTextColor(Color.WHITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snack.dismiss();
                        }
                    });
            snack.getView().setBackgroundColor(Color.RED);
            snack.show();
        }
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
                GPSTracker gps = new GPSTracker(this);
                if(gps.canGetLocation()){
                    double currentLatitude = gps.getLatitude();
                    double currentLongitude = gps.getLongitude();
                    //fetch the city that has these coordinates:
                    CityRequestTask task = new CityRequestTask(this);
                    task.execute(String.valueOf(currentLatitude),String.valueOf(currentLongitude));
                }
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

    private void fillWeatherData(){
        if(cityWeather!=null && !cityWeather.getWeatherList().isEmpty()){
            fillWeatherNowCard();
            fillForecastCard();
        }else{
            if(adapter != null){
                adapter.clear();
            }
        }
    }

    private void fillWeatherNowCard(){
        final Weather weather = cityWeather.getWeatherAt(0);
        city_tv.setText(cityWeather.getLocation().getCity()
                        + ", " + cityWeather.getLocation().getCountry());
        day_tv.setText(weather.getDate().dayOfWeek().getAsText(Locale.US));
        descr_tv.setText(weather.getCurrentCondition().getCondition() +
                " - " + weather.getCurrentCondition().getDescription());
        LayoutUtils utils = LayoutUtils.getInstance().init(this);
        String temp = utils.getConvertedTemperatureWithUnit(weather.getTemperatureNow(), this);
        temp_tv.setText(temp);
        String max_temp = utils.getConvertedTemperatureWithUnit(weather.getTemperature().getMaxTemp(), this);
        String min_temp = utils.getConvertedTemperatureWithUnit(weather.getTemperature().getMinTemp(), this);
        temp_high_tv.setText(getString(R.string.high_txt) + max_temp);
        temp_low_tv.setText(getString(R.string.low_txt) + min_temp);
        weather_today_img.setImageBitmap(weather.getIcon());
        View cardFrame = findViewById(R.id.card_frame);
        cardFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeekForecastActivity.this, DetailsActivity.class);
                intent.putExtra("weather",weather);
                intent.putExtra("city",cityWeather.getLocation().getCity());
                intent.putExtra("country",cityWeather.getLocation().getCountry());
                startActivity(intent);
            }
        });
    }

    private void fillForecastCard(){
        adapter = new ForecastAdapter(this,
                cityWeather.getWeatherList(),
                cityWeather.getLocation().getCity(),
                cityWeather.getLocation().getCountry());
        forecastRecyclerView.setAdapter(adapter);
    }

    private void showWeatherDataInDBFor(String cityAndCountry){
        if(cityAndCountry != null){
            cityWeather = DBHelper.getInstance(this).findCityWeather(cityAndCountry);
            fillWeatherData();
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
     * When we get the city with given coordinates:
     */
    @Override
    public void onCityRetrieved(List<String> output) {
        if(!output.isEmpty()){
            //This list will only have one city:
            String cityAndCountry = output.get(0);
            //Save it as the last city used by the app:
            prefs.edit().putString(getString(R.string.key_last_location),
                    cityAndCountry).apply();
            //request updated weather online:
            WeatherRequestTask task = new WeatherRequestTask(this);
            task.execute(cityAndCountry);
        }
    }

    @Override
    public void onCityWeatherRetrieved(CityWeather output) {
        if(output!=null){
            cityWeather = output;
            DBHelper helper = DBHelper.getInstance(this);
            //save the new retrieved data:
            helper.save(cityWeather);
            fillWeatherData();
        }
    }
}
