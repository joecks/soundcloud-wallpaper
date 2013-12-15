package de.halfreal.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import de.halfreal.model.Track;
import de.halfreal.net.SoundwaveServiceModule.SoundwaveModuleListener;

public class SoundwaveWalpaperService extends WallpaperService {

	private class SoundwaveEngine extends Engine implements Target,
			SoundwaveModuleListener {

		private Bitmap waveBitmap;

		public void newTrackReceived(Track track) {
			Picasso.with(getApplicationContext()).load(track.getWaveform_url())
					.into(this);
		}

		public void onBitmapFailed(Drawable arg0) {
		}

		public void onBitmapLoaded(Bitmap arg0, LoadedFrom arg1) {
			waveBitmap = arg0;
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			SoundwaveApplication.soundwaveModule.registerListener(this);
			SoundwaveApplication.soundwaveModule.downloadNewWaveform();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			SoundwaveApplication.soundwaveModule.unregisterListener(this);
		}

		public void onPrepareLoad(Drawable arg0) {
		}

	}

	@Override
	public Engine onCreateEngine() {
		return new SoundwaveEngine();
	}

}
