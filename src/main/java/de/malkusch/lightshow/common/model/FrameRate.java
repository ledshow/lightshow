package de.malkusch.lightshow.common.model;

public final class FrameRate {

	private final int fps;

	public FrameRate(int framesPerSecond) {
		if (framesPerSecond <= 0) {
			throw new IllegalArgumentException("must be positiv");
		}
		this.fps = framesPerSecond;
	}

	public int framesPerSecond() {
		return fps;
	}

	public int frameMilliseconds() {
		return (int) Math.round(1000.0 / fps);
	}

}
