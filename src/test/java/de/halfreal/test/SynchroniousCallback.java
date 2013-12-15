package de.halfreal.test;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SynchroniousCallback<T> implements Callback<T> {

	private T data;
	private RetrofitError error;
	private boolean isCalled;
	private Response response;

	public SynchroniousCallback() {
		isCalled = false;
	}

	public void failure(RetrofitError error) {
		this.error = error;
		isCalled = true;
	}

	public T getData() {
		return data;
	}

	public RetrofitError getError() {
		return error;
	}

	public Response getResponse() {
		return response;
	}

	public boolean isCalled() {
		return isCalled;
	}

	public void setCalled(boolean isCalled) {
		this.isCalled = isCalled;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void setError(RetrofitError error) {
		this.error = error;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public void success(T data, Response response) {
		this.data = data;
		this.response = response;
		isCalled = true;
	}

}
