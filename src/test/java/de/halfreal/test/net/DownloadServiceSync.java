package de.halfreal.test.net;

import retrofit.http.GET;
import retrofit.http.Path;

public interface DownloadServiceSync {

	@GET("{url}")
	byte[] downloadImage(@Path("url") String url);

}
