package com.nanoconverter.zlab;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;

public class Preferences extends PreferenceActivity {
	
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        	
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.preferences);

                getPreferenceManager()
                .findPreference("sourceurl")
                .setOnPreferenceClickListener(
                   new OnPreferenceClickListener() {
                 public boolean onPreferenceClick(Preference preference) {
                     Intent intent = new Intent(Intent.ACTION_VIEW);
                     intent.setData(Uri.parse("http://zorger.github.com/nanoConverter/"));
                     startActivity(intent);
                     return true;
                 }
             });

        }
}