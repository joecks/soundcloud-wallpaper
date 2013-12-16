package de.halfreal.net;

import java.util.HashSet;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.util.Log;
import de.halfreal.model.Track;
import de.halfreal.ui.SoundwaveApplication;

public class SoundwaveServiceModule {

	public static interface SoundwaveModuleListener {

		void newTrackReceived(Track track);

	}

	private Set<SoundwaveModuleListener> listeners;
	private String user;

	public SoundwaveServiceModule() {
		listeners = new HashSet<SoundwaveModuleListener>();
		user = "zoekeating";
	}

	public void downloadNewWaveform() {

		SoundwaveApplication.trackService.tracks(API.CLIENT_ID, user,
				new Callback<Track[]>() {

					private String tag = this.getClass().getName();

					public void failure(RetrofitError error) {
						Log.e(tag, "Faild: " + error.getLocalizedMessage());
					}

					public void success(Track[] tracks, Response response) {

						Track track = tracks[(int) (Math.random() * tracks.length)];

						for (SoundwaveModuleListener listener : listeners) {
							listener.newTrackReceived(track);
						}
					}
				});

	}

	public void registerListener(SoundwaveModuleListener listener) {
		System.out.println("registered listener");
		listeners.add(listener);
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void unregisterListener(SoundwaveModuleListener listener) {
		System.out.println("unregistered listener");
		listeners.remove(listener);
	}

}
