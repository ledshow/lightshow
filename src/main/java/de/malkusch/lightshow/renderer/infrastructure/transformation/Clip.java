package de.malkusch.lightshow.renderer.infrastructure.transformation;

import de.malkusch.lightshow.common.model.Duration;
import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.common.model.Position;
import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class Clip extends Transformation {

	private final Transformation transformation;

	public Clip(Duration duration, Transformation transformation) {
		this(transformation.start(), duration, transformation);
	}

	public Clip(Position start, Duration duration, Transformation transformation) {
		super(transformation.lightId(), start, duration);
		this.transformation = transformation;

		var offset = start.frame() - transformation.start().frame();
		if (offset < 0) {
			throw new IllegalArgumentException("Must not be negative");
		}
		if (offset >= transformation.duration().frames()) {
			throw new IllegalArgumentException("Start exceeds transformation");
		}
		if (offset + duration.frames() > transformation.duration().frames()) {
			throw new IllegalArgumentException("End exceeds transformation");
		}
	}

	@Override
	public Transformation with(LightId lightId) {
		return new Clip(start(), duration(), transformation.with(lightId));
	}

	@Override
	public Transformation with(Position start) {
		return new Clip(start, duration(), transformation);
	}

	@Override
	public AlphaColor transform(FrameRate rate, Position position) {
		return transformation.transform(rate, position);
	}

}
