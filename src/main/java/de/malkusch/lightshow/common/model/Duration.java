package de.malkusch.lightshow.common.model;

import static java.util.Objects.requireNonNull;

public final class Duration {

	private final long frames;

	public Duration(long frames) {
		if (frames <= 0) {
			throw new IllegalArgumentException("Duration must be positive");
		}
		this.frames = frames;
	}

	public Duration add(Duration duration) {
		return new Duration(requireNonNull(duration).frames + frames);
	}

	public Duration dividedBy(int divisor) {
		if (divisor <= 0) {
			throw new IllegalArgumentException("Divisor must be positive");
		}
		return new Duration(Math.round(frames / (double) divisor));
	}

	public long frames() {
		return frames;
	}

	@Override
	public String toString() {
		return String.format("%d frames", frames);
	}

	@Override
	public int hashCode() {
		return Long.hashCode(frames);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Duration)) {
			return false;
		}
		var other = (Duration) obj;
		return frames == other.frames;
	}

}
