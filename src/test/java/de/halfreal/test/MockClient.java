package de.halfreal.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.mime.TypedByteArray;
import android.net.Uri;

public class MockClient implements Client {

	public static String convertStreamToString(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public Response execute(Request request) throws IOException {

		Uri uri = Uri.parse(request.getUrl());

		String responseString = "JSON STRING HERE";
		if (uri.getPath().contains("tracks.json")) {
			responseString = convertStreamToString(getClass()
					.getResourceAsStream("/tracks.json"));
			System.out.println(responseString);
		}

		return new Response(200, "nothing", Collections.<Header> emptyList(),
				new TypedByteArray("application/json",
						responseString.getBytes()));
	}
}
