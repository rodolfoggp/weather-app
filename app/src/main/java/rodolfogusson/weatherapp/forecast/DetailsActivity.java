package rodolfogusson.weatherapp.forecast;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.LocalDate;

import java.util.Locale;

import rodolfogusson.weatherapp.R;
import rodolfogusson.weatherapp.model.Weather;
import rodolfogusson.weatherapp.utilities.LayoutUtils;

public class DetailsActivity extends AppCompatActivity {

    Weather weather;
    String city, country;
    LayoutUtils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        weather = getIntent().getParcelableExtra("weather");
        city = getIntent().getExtras().getString("city");
        country = getIntent().getExtras().getString("country");
        utils = LayoutUtils.getInstance().init(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView city_tv = (TextView) findViewById(R.id.city);
        TextView day_tv = (TextView) findViewById(R.id.day_of_week);
        TextView temp_condition_tv = (TextView) findViewById(R.id.temp_now_or_condition);
        TextView description_tv = (TextView) findViewById(R.id.description);
        ImageView weather_img = (ImageView) findViewById(R.id.weather_img);

        //Upper block:
        city_tv.setText(city+ ", " + country);
        day_tv.setText(weather.getDate().dayOfWeek().getAsText(Locale.US));
        weather_img.setImageBitmap(weather.getIcon());
        if(weather.getDate().isEqual(new LocalDate())){ //if this weather is for today
            String temp = utils.getConvertedTemperatureWithUnit(weather.getTemperatureNow());
            temp_condition_tv.setText(temp);
            description_tv.setText(weather.getCurrentCondition().getCondition() + " - " +
                    weather.getCurrentCondition().getDescription());
        }else{
            temp_condition_tv.setText(weather.getCurrentCondition().getCondition());
        }

        //High temp block:
        fillBlock(R.id.high_block,
                R.drawable.ic_temperature,
                R.string.high_txt,
                utils.getConvertedTemperatureWithUnit(weather.getTemperature().getMaxTemp()));

        //Low temp block:
        fillBlock(R.id.low_block,
                R.drawable.ic_temperature,
                R.string.low_txt,
                utils.getConvertedTemperatureWithUnit(weather.getTemperature().getMinTemp()));

        //Pressure block:
        fillBlock(R.id.pressure_block,
                R.drawable.ic_pressure,
                R.string.pressure_txt,
                utils.getPressureWithUnit(weather.getCurrentCondition().getPressure()));

        //Humidity block:
        fillBlock(R.id.humidity_block,
                R.drawable.ic_humidity,
                R.string.humidity_txt,
                utils.getHumidityWithUnit(weather.getCurrentCondition().getHumidity()));
    }

    private void fillBlock(int blockId, int drawableId, int textId, String value){
        View block = findViewById(blockId);
        ((ImageView)block.findViewById(R.id.icon)).setImageDrawable(getDrawable(drawableId));
        ((TextView)block.findViewById(R.id.title)).setText(getString(textId));
        ((TextView)block.findViewById(R.id.value)).setText(value);
    }
}
