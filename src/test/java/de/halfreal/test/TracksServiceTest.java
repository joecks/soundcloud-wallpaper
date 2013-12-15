package de.halfreal.test;

import static junit.framework.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit.RestAdapter;
import de.halfreal.model.Track;
import de.halfreal.net.API;
import de.halfreal.test.net.TrackServiceSync;
import de.halfreal.test.robolectric.RobolectricMavenTestRunner;

@RunWith(RobolectricMavenTestRunner.class)
public class TracksServiceTest {

	public TrackServiceSync createMockTrackService() {

		RestAdapter restAdapter = new RestAdapter.Builder()
				.setServer(API.SOUNDCLOUD_URL).setClient(new MockClient())
				.build();

		return restAdapter.create(TrackServiceSync.class);

	}

	@Test
	public void queryForTracksFromMockServiceTest() throws InterruptedException {
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
}
