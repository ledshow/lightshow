package de.malkusch.lightshow.renderer.model;

public final class Duration {

	private final long frames;

	public Duration(long frames) {
		if (frames <= 0) {
			throw new IllegalArgumentException("Duration must be positive");
		}
		this.frames = frames;
	}

	public long frames() {
		return frames;
	}

	@Override
	public String toString() {
		return Long.toString(frames);
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
