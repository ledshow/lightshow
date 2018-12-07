package de.malkusch.lightshow.renderer.model;

import static java.util.Objects.requireNonNull;

import de.malkusch.lightshow.common.model.FrameRate;

public abstract class Transformation {

	protected Transformation(LightId lightId, Position start, Duration duration) {
		this.lightId = requireNonNull(lightId);
		this.start = requireNonNull(start);
		this.duration = requireNonNull(duration);
	}

	private final LightId lightId;

	public final LightId lightId() {
		return lightId;
	}

	abstract public AlphaColor transform(FrameRate rate, Position position);

	private final Position start;

	public final Position start() {
		return start;
	}

	protected Position relativePosition(Position position) {
		return position.minus(start);
	}

	private final Duration duration;

	public final Duration duration() {
		return duration;
	}

	public final Position end() {
		return new Position(start.frame() + duration.frames() - 1);
	}

	public final boolean isActive(Position position) {
		return position.isWithin(start, end());
	}

}
