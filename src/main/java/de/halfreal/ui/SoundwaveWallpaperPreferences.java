package de.halfreal.ui;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import de.halfreal.R;

public class SoundwaveWallpaperPreferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefernces);
	}

}
