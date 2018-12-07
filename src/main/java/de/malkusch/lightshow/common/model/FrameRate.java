package de.malkusch.lightshow.common.model;

import de.malkusch.lightshow.renderer.model.Position;

public final class FrameRate {

	private final int fps;

	public FrameRate(int framesPerSecond) {
		if (framesPerSecond <= 0) {
			throw new IllegalArgumentException("must be positiv");
		}
		this.fps = framesPerSecond;
	}

	public Position position(int second, int millisecond) {
		return new Position(fps * second + frameMilliseconds() * millisecond);
	}

	public int framesPerSecond() {
		return fps;
	}

	public int frameMilliseconds() {
		return (int) Math.round(1000.0 / fps);
	}

	@Override
	public String toString() {
		return Integer.toString(fps);
	}

}
