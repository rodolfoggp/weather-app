package rodolfogusson.weatherapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

/**
 * Created by rodolfo on 5/22/17.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    Preference location, apiKey;
    SharedPreferences spf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, false);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        spf = PreferenceManager.getDefaultSharedPreferences(getContext());

        //Location preference:
        location = findPreference(getString(R.string.key_location));
        location.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), SearchCityActivity.class);
                startActivity(intent);
                return true;
            }
        });

        //API Key preference:
        apiKey = findPreference(getString(R.string.key_api_key));
    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);

        String locationStr = spf.getString(getString(R.string.key_location),getString(R.string.current_location));
        location.setSummary(locationStr);
        String apikeyStr = spf.getString(getString(R.string.key_api_key),null);
        apiKey.setSummary(apikeyStr);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updatePreference(findPreference(key), key);
    }

    private void updatePreference(Preference preference, String key) {
        if (preference == null) return;
        preference.setSummary(spf.getString(key,null));
    }
}
