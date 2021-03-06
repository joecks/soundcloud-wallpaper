package de.halfreal.test;

import static junit.framework.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit.Callback;
import retrofit.RestAdapter;
import de.halfreal.model.Track;
import de.halfreal.net.API;
import de.halfreal.net.SoundwaveServiceModule;
import de.halfreal.net.SoundwaveServiceModule.SoundwaveModuleListener;
import de.halfreal.net.TrackService;
import de.halfreal.test.net.TrackServiceSync;
import de.halfreal.test.robolectric.RobolectricMavenTestRunner;
import de.halfreal.ui.SoundwaveApplication;

@RunWith(RobolectricMavenTestRunner.class)
public class TracksServiceTest {

	protected Boolean called;

	@Before
	public void beforeTest() {
		called = null;
	}

	public TrackServiceSync createMockTrackService() {

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setServer(API.SOUNDCLOUD_URL).setClient(new MockClient())
				.build();

		return restAdapter.create(TrackServiceSync.class);

	}

	public TrackService createMockTrackServiceAsync() {

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setServer(API.SOUNDCLOUD_URL).setClient(new MockClient())
				.build();

		return restAdapter.create(TrackService.class);

	}

	@Test
	public void testJSONTOModelDeserialization() throws InterruptedException {
		TrackServiceSync createMockTrackService = createMockTrackService();
		Track[] tracks = createMockTrackService.tracks(API.CLIENT_ID,
				"zoekeating");
		assertNotNull(tracks);
		assertNotNull(tracks[0]);
		assertNotNull(tracks[0].getPermalink_url());
		assertNotNull(tracks[0].getTitle());
		assertNotNull(tracks[0].getWaveform_url());
		assertEquals("http://w1.sndcdn.com/MEkayzDwTueX_m.png",
				tracks[0].getWaveform_url());
	}

	@Test
	public void testModuleListeners() {

		SoundwaveApplication.trackService = new TrackService() {

			public void tracks(String clientId, String user,
					Callback<Track[]> response) {
				response.success(null, null);
			}
		};
		SoundwaveServiceModule soundwaveServiceModule = new SoundwaveServiceModule();
		SoundwaveModuleListener listener = new SoundwaveModuleListener() {

			public void newTrackReceived(Track track) {
				called = true;
			}
		};

		soundwaveServiceModule.registerListener(listener);
		soundwaveServiceModule.downloadNewWaveform();

		assertNull(called);

		SoundwaveApplication.trackService = new TrackService() {

			public void tracks(String clientId, String user,
					Callback<Track[]> response) {
				Track[] tracks = new Track[] { new Track() };
				response.success(tracks, null);
			}
		};
		soundwaveServiceModule.downloadNewWaveform();

		assertTrue(called);
		called = null;

		soundwaveServiceModule.unregisterListener(listener);
		soundwaveServiceModule.downloadNewWaveform();
		assertNull(called);
	}

}
