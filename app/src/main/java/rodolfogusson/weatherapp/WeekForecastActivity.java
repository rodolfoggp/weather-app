package rodolfogusson.weatherapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONObject;

import rodolfogusson.weatherapp.model.Weather;

public class WeekForecastActivity extends AppCompatActivity implements WeatherRequestTask.AsyncResponse{

    Weather weather;
    TextView city_tv, descr_tv, temp_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_forecast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String city = "Rome,IT";

        city_tv = (TextView) findViewById(R.id.city_text);
        descr_tv = (TextView) findViewById(R.id.description);
        temp_tv = (TextView) findViewById(R.id.temp);

        WeatherRequestTask task = new WeatherRequestTask(this);
        task.execute(city);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(weather!=null){
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillData(){
        city_tv.setText(weather.getLocation().getCity());
        descr_tv.setText(weather.getCurrentCondition().getDescription());
        temp_tv.setText(String.valueOf(weather.getTemperature().getTemp()));
    }

    @Override
    public void processFinish(Weather output) {
        weather = output;
        fillData();
    }
}
