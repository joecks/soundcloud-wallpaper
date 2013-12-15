package de.halfreal.net;

import retrofit.RestAdapter;

public class API {

	public static final String CLIENT_ID = "be7e3f86765025dcaf806225f08a4865";

	public static final String SOUNDCLOUD_URL = "http://api.soundcloud.com/";

	public static TrackService createTrackService() {

		RestAdapter restAdapter = new RestAdapter.Builder().setServer(
				SOUNDCLOUD_URL).build();
		return restAdapter.create(TrackService.class);
	}

}
