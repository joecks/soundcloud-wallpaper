package de.halfreal.test.net;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import de.halfreal.model.Track;

public interface TrackServiceSync {

	@GET("/users/{user}/tracks.json")
	Track[] tracks(@Query("client_id") String clientId,
			@Path("user") String user);

}
