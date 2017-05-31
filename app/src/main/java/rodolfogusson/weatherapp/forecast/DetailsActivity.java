package rodolfogusson.weatherapp.forecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rodolfogusson.weatherapp.R;
import rodolfogusson.weatherapp.model.Weather;

public class DetailsActivity extends AppCompatActivity {

    final Weather weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }
}
