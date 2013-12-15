package de.halfreal.test.net;

import retrofit.http.GET;
import retrofit.http.Path;
import android.provider.ContactsContract.CommonDataKinds.Photo;

public interface DownloadServiceSync {

	@GET("{url}")
	Photo downloadPhoto(@Path("url") String url);

}
