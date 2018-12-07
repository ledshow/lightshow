package de.malkusch.lightshow.renderer.model;

import static java.util.Objects.requireNonNull;

public final class Position {

	private final long frame;

	public Position(long frame) {
		if (frame < 0) {
			throw new IllegalArgumentException("Position must not be negative");
		}
		this.frame = frame;
	}

	public long frame() {
		return frame;
	}

	public boolean isAfter(Position position) {
		requireNonNull(position);
		return frame > position.frame;
	}

	public Position next() {
		return new Position(frame + 1);
	}

	public Position minus(Position position) {
		requireNonNull(position);
		if (position.frame > frame) {
			throw new IllegalArgumentException("Invalid substraction");
		}
		return new Position(frame - position.frame);
	}

	@Override
	public String toString() {
		return Long.toString(frame);
	}

	@Override
	public int hashCode() {
		return Long.hashCode(frame);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Position)) {
			return false;
		}
		var other = (Position) obj;
		return frame == other.frame;
	}

	public boolean isWithin(Position start, Position end) {
		requireNonNull(start);
		requireNonNull(end);
		if (!end.isAfter(start)) {
			throw new IllegalArgumentException("End must be after start");
		}
		return frame >= start.frame && frame <= end.frame;
	}

}
