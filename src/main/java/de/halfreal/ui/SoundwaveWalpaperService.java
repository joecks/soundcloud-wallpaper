package de.halfreal.ui;

import android.service.wallpaper.WallpaperService;

public class SoundwaveWalpaperService extends WallpaperService {

	private class SoundwaveEngine extends Engine {

	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public Engine onCreateEngine() {
		return new SoundwaveEngine();
	}

}
