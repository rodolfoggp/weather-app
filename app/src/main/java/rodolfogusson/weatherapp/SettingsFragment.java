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

public class SettingsFragment extends PreferenceFragmentCompat {
    Preference location;
    SharedPreferences spf;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preferences);

        spf = PreferenceManager.getDefaultSharedPreferences(getContext());

        //Location preference:
        location = findPreference(getString(R.string.key_location));
        location.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), SearcheableActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        String locationStr = spf.getString(getString(R.string.key_location),null);
        location.setSummary(locationStr);
    }
}
