package hr.danijelbabic.alarmclockapp.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import hr.danijelbabic.alarmclockapp.R;


public class MyPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceValue) {
        super.onCreate(savedInstanceValue);
        addPreferencesFromResource(R.xml.app_settings);
    }

}
