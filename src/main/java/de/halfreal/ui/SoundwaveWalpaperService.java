package de.halfreal.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.service.wallpaper.WallpaperService;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import de.halfreal.model.Track;
import de.halfreal.net.SoundwaveServiceModule.SoundwaveModuleListener;

public class SoundwaveWalpaperService extends WallpaperService implements
		SoundwaveModuleListener {

	private class SoundwaveEngine extends Engine implements Target {

		private Bitmap waveBitmap;

		public void onBitmapFailed(Drawable arg0) {
		}

		public void onBitmapLoaded(Bitmap arg0, LoadedFrom arg1) {
			waveBitmap = arg0;
		}

		public void onPrepareLoad(Drawable arg0) {
		}

	}

	private SoundwaveEngine engine;

	public void newTrackReceived(Track track) {
		Picasso.with(getApplicationContext()).load(track.getWaveform_url())
				.into(engine);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		SoundwaveApplication.soundwaveModule.registerListener(this);
		SoundwaveApplication.soundwaveModule.downloadNewWaveform();
	}

	@Override
	public Engine onCreateEngine() {
		engine = new SoundwaveEngine();
		return engine;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		SoundwaveApplication.soundwaveModule.unregisterListener(this);
	}

}
