package hr.danijelbabic.alarmclockapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import hr.danijelbabic.alarmclockapp.fragments.MyPreferenceFragment;


public class MyPreferenceActivity  extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new MyPreferenceFragment())
                .commit();
    }
}
