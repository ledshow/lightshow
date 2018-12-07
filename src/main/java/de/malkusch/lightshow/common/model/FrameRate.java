package de.malkusch.lightshow.common.model;

import static java.lang.Math.round;

import de.malkusch.lightshow.renderer.model.Duration;
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
		return new Position(fps * second + round(fps / 1000.0 * millisecond));
	}

	public Duration duration(int seconds, int milliseconds) {
		return new Duration(fps * seconds + round(fps / 1000.0 * milliseconds));
	}

	public int framesPerSecond() {
		return fps;
	}

	public int frameMilliseconds() {
		return (int) round(1000.0 / fps);
	}

	@Override
	public String toString() {
		return Integer.toString(fps);
	}

}
