package de.halfreal.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
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

	private byte[] convertStramToByteArray() throws FileNotFoundException,
			IOException {
		RandomAccessFile f = null;
		try {
			f = new RandomAccessFile("/soundwave.png", "r");
			byte[] b = new byte[(int) f.length()];
			f.read(b);
			return b;
		} finally {
			f.close();
		}
	}

	public Response execute(Request request) throws IOException {

		Uri uri = Uri.parse(request.getUrl());

		String responseString = "JSON STRING HERE";
		String path = uri.getPath();
		if (path.contains("tracks.json")) {
			responseString = convertStreamToString(getClass()
					.getResourceAsStream("/tracks.json"));
			return new Response(200, "nothing",
					Collections.<Header> emptyList(), new TypedByteArray(
							"application/json", responseString.getBytes()));
		} else if (path.contains(".png")) {
			byte[] bytes = convertStramToByteArray();
			new Response(200, "nothing", Collections.<Header> emptyList(),
					new TypedByteArray("image/png", bytes));
		}

		return new Response(400, "bad request",
				Collections.<Header> emptyList(), null);
	}
}
