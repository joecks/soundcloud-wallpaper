package de.halfreal.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import de.halfreal.model.Track;
import de.halfreal.net.SoundwaveServiceModule.SoundwaveModuleListener;

public class SoundwaveWalpaperService extends WallpaperService {

	private class SoundwaveEngine extends Engine implements Target,
			SoundwaveModuleListener {

		private static final int STATE_BROWSER = 2;
		private static final int STATE_NONE = 0;
		private static final int STATE_TITLE = 1;
		private Rect dst;
		private Handler handler;
		private int height;

		private Paint paint;

		private Runnable redrawRunable;
		private int state;
		private Track track;
		private Bitmap waveBitmap;
		private int width;
		private int xPixelOffset;

		public SoundwaveEngine() {
			paint = new Paint();
			paint.setColor(Color.WHITE);

			handler = new Handler();
			redrawRunable = new Runnable() {

				public void run() {
					redraw();
				}
			};
			state = 0;

		}

		public void newTrackReceived(Track track) {
			this.track = track;
			state = STATE_NONE;
			Picasso.with(getApplicationContext()).load(track.getWaveform_url())
					.into(this);
		}

		public void onBitmapFailed(Drawable arg0) {
		}

		public void onBitmapLoaded(Bitmap newBitmap, LoadedFrom arg1) {
			recycleBitmap();
			waveBitmap = newBitmap;

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
			recycleBitmap();
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

		@Override
		public void onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);
			if (event.getAction() == MotionEvent.ACTION_UP && dst != null
					&& dst.contains((int) event.getX(), (int) event.getY())) {
				if (state == STATE_BROWSER) {
					startBrowser();
				}
				state = (state + 1) % 3;
				refresh();
			}
			System.out.println("Touch " + event);
			System.out.println("DST " + dst);
		}

		private void recycleBitmap() {
			if (waveBitmap != null) {
				waveBitmap.recycle();
			}
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
					dst = new Rect(0, height / 2 - src.height() / 2, width,
							height / 2 + src.height() / 2);
					canvas.drawBitmap(waveBitmap, src, dst, paint);

					if (state >= STATE_TITLE) {
						canvas.drawText(String.format("%s - %s", track
								.getUser().getUsername(), track.getTitle()),
								20, dst.bottom + 20, paint);
					}
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

		private void startBrowser() {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(track.getPermalink_url()));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}

	}

	public static final long TIME_TILL_NEXT_UPDATE = 60 * 1000 * 30;

	@Override
	public Engine onCreateEngine() {
		return new SoundwaveEngine();
	}

}
