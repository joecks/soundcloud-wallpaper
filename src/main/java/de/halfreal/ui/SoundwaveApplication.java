package de.halfreal.ui;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import de.halfreal.R;
import de.halfreal.net.API;
import de.halfreal.net.SoundwaveServiceModule;
import de.halfreal.net.TrackService;

public class SoundwaveApplication extends Application implements
		OnSharedPreferenceChangeListener {

	public static SoundwaveServiceModule soundwaveModule;
	public static TrackService trackService;

	@Override
	public void onCreate() {
		super.onCreate();
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
		String userName = PreferenceManager.getDefaultSharedPreferences(this)
				.getString(getString(R.string.key_userName), "zoekeating");
		trackService = API.createTrackService();
		soundwaveModule = new SoundwaveServiceModule();
		soundwaveModule.setUser(userName);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		String userName = PreferenceManager.getDefaultSharedPreferences(this)
				.getString(getString(R.string.key_userName), "zoekeating");
		soundwaveModule.setUser(userName);
		soundwaveModule.downloadNewWaveform();
	}

}
