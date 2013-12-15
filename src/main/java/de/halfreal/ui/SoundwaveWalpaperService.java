package de.halfreal.ui;

import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.service.wallpaper.WallpaperService;
import de.halfreal.net.SoundwaveServiceModule.SoundwaveModuleListener;

public class SoundwaveWalpaperService extends WallpaperService implements
		SoundwaveModuleListener {

	private class SoundwaveEngine extends Engine {

	}

	public void newPhotoReceived(Photo photo) {

	}

	@Override
	public void onCreate() {
		super.onCreate();
		SoundwaveApplication.soundwaveModule.registerListener(this);
		SoundwaveApplication.soundwaveModule.downloadNewWaveform();
	}

	@Override
	public Engine onCreateEngine() {
		return new SoundwaveEngine();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		SoundwaveApplication.soundwaveModule.unregisterListener(this);
	}

}
