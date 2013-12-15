package de.halfreal.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
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

		private Handler handler;
		private int height;
		private Paint paint;
		private Runnable redrawRunable;
		private Bitmap waveBitmap;
		private int width;
		private int xPixelOffset;

		public SoundwaveEngine() {
			paint = new Paint();
			handler = new Handler();
			redrawRunable = new Runnable() {

				public void run() {
					redraw();
				}
			};
		}

		public void newTrackReceived(Track track) {
			Picasso.with(getApplicationContext()).load(track.getWaveform_url())
					.into(this);
			System.out.println("new track: " + track.getWaveform_url());
		}

		public void onBitmapFailed(Drawable arg0) {
			System.out.println("faild loading bitmap");
		}

		public void onBitmapLoaded(Bitmap newBitmap, LoadedFrom arg1) {
			waveBitmap = newBitmap;
			System.out.println("new Bitmap: " + newBitmap.getWidth());
			refresh();
		}

		@Override
		public void onCreate(SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			SoundwaveApplication.soundwaveModule.registerListener(this);
			SoundwaveApplication.soundwaveModule.downloadNewWaveform();
			scheduleUpdate();
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			SoundwaveApplication.soundwaveModule.unregisterListener(this);
		}

		@Override
		public void onOffsetsChanged(float xOffset, float yOffset,
				float xOffsetStep, float yOffsetStep, int xPixelOffset,
				int yPixelOffset) {
			this.xPixelOffset = xPixelOffset;
			super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
					xPixelOffset, yPixelOffset);
			refresh();
		}

		public void onPrepareLoad(Drawable arg0) {
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			this.width = width;
			this.height = height;
			super.onSurfaceChanged(holder, format, width, height);

		}

		private void redraw() {
			SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				if (canvas != null && waveBitmap != null) {
					canvas.drawColor(Color.BLACK);
					Rect src = new Rect(-xPixelOffset,
							waveBitmap.getHeight() / 2, width - xPixelOffset,
							waveBitmap.getHeight());
					Rect dst = new Rect(0, height / 2 - src.height() / 2,
							width, height / 2 + src.height() / 2);
					canvas.drawBitmap(waveBitmap, src, dst, paint);
				}
			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
		}

		private void refresh() {
			handler.post(redrawRunable);
		}

		private void scheduleUpdate() {
			handler.postDelayed(new Runnable() {

				public void run() {
					SoundwaveApplication.soundwaveModule.downloadNewWaveform();
					scheduleUpdate();
				}
			}, TIME_TILL_NEXT_UPDATE);
		}
	}

	public static final long TIME_TILL_NEXT_UPDATE = 60 * 1000 * 30;

	@Override
	public Engine onCreateEngine() {
		return new SoundwaveEngine();
	}

}
