package de.halfreal.model;

public class Track {

	private String permalink_url;
	private String title;
	private User user;
	private String waveform_url;

	public String getPermalink_url() {
		return permalink_url;
	}

	public String getTitle() {
		return title;
	}

	public User getUser() {
		return user;
	}

	public String getWaveform_url() {
		return waveform_url;
	}

	public void setPermalink_url(String permalink_url) {
		this.permalink_url = permalink_url;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setWaveform_url(String waveform_url) {
		this.waveform_url = waveform_url;
	}

}
