package de.malkusch.lightshow.renderer.model;

import static java.util.Objects.requireNonNull;

public abstract class Transformation {

	protected Transformation(LightId lightId, Position start, Position end) {
		this.lightId = requireNonNull(lightId);
		this.start = requireNonNull(start);
		this.end = requireNonNull(end);
		if (!end.isAfter(start)) {
			throw new IllegalArgumentException("End must be after start");
		}
	}

	private final LightId lightId;

	public final LightId lightId() {
		return lightId;
	}

	abstract public AlphaColor transform(Position position);

	private final Position start;

	public final Position start() {
		return start;
	}

	private final Position end;

	public final Position end() {
		return end;
	}

	public final boolean isActive(Position position) {
		return position.isWithin(start, end);
	}

}
