package de.halfreal.net;

import java.util.HashSet;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import de.halfreal.model.Track;
import de.halfreal.model.TrackResponse;
import de.halfreal.ui.SoundwaveApplication;

public class SoundwaveServiceModule {

	public static interface SoundwaveModuleListener {

		void newTrackReceived(Track track);

	}

	private Set<SoundwaveModuleListener> listeners;

	public SoundwaveServiceModule() {
		listeners = new HashSet<SoundwaveModuleListener>();
	}

	public void downloadNewWaveform() {

		SoundwaveApplication.trackService.tracks(API.CLIENT_ID, "zoekeating",
				new Callback<TrackResponse>() {

					public void failure(RetrofitError error) {
					}

					public void success(TrackResponse trackResponse,
							Response response) {

						Track track = trackResponse.getTracks()[(int) (Math
								.random() * trackResponse.getTracks().length)];

						for (SoundwaveModuleListener listener : listeners) {
							listener.newTrackReceived(track);
						}
					}
				});

	}

	public void registerListener(SoundwaveModuleListener listener) {
		listeners.add(listener);
	}

	public void unregisterListener(SoundwaveModuleListener listener) {
		listeners.remove(listener);
	}

}
