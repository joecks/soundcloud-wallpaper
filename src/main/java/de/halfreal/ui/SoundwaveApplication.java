package de.halfreal.ui;

import android.app.Application;
import de.halfreal.net.API;
import de.halfreal.net.SoundwaveServiceModule;
import de.halfreal.net.TrackService;

public class SoundwaveApplication extends Application {

	public static SoundwaveServiceModule soundwaveModule;
	public static TrackService trackService;

	@Override
	public void onCreate() {
		super.onCreate();
		trackService = API.createTrackService();
		soundwaveModule = new SoundwaveServiceModule();
	}

}
