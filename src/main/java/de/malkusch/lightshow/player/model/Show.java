package de.malkusch.lightshow.player.model;

import static java.util.Objects.requireNonNull;

public final class Show {

	private final AudioStream audioStream;
	private final DmxStream dmxStream;

	public Show(AudioStream audioStream, DmxStream dmxStream) {
		this.audioStream = requireNonNull(audioStream);
		this.dmxStream = requireNonNull(dmxStream);
	}

	public AudioStream audioStream() {
		return audioStream;
	}

	public DmxStream dmxStream() {
		return dmxStream;
	}

}
