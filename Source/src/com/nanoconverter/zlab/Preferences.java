package com.nanoconverter.zlab;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class Preferences extends PreferenceActivity {
	
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        	
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.preferences);

                // Get the custom preference
                /*
                final ListPreference prBankLst = (ListPreference) getPreferenceManager().findPreference("listSourcesDefault");
                prBankLst.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

					public boolean onPreferenceChange(Preference preference, Object newValue) {
							// TODO Auto-generated method stub
						
						return false;
					}
				});
                */
                
        }
}