package ca.tru.comp2160.kanamatch;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class OptionsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        //setTheme(R.style.PreferencesStyle);
        
        addPreferencesFromResource(R.xml.settings);
        
    }
}
