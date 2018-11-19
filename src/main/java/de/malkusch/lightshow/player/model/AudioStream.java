package de.malkusch.lightshow.player.model;

import static java.util.Objects.requireNonNull;

import java.io.InputStream;

public final class AudioStream {

	private final InputStream stream;

	public AudioStream(InputStream stream) {
		this.stream = requireNonNull(stream);
	}

	public InputStream stream() {
		return stream;
	}

}
