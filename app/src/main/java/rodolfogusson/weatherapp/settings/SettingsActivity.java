package rodolfogusson.weatherapp.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import rodolfogusson.weatherapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Display the fragment as the main content.
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new SettingsFragment())
                .commit();
    }
}
