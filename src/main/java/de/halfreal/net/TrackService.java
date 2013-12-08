package de.halfreal.net;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import de.halfreal.model.TrackResponse;

public interface TrackService {

	@GET("/users/{user}/tracks.json")
	void tracks(@Query("client_id") String clientId, @Path("user") String user,
			Callback<TrackResponse> response);

}
