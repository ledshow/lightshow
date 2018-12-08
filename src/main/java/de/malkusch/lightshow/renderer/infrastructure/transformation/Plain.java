package de.malkusch.lightshow.renderer.infrastructure.transformation;

import static java.util.Objects.requireNonNull;

import de.malkusch.lightshow.common.model.Duration;
import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.common.model.Position;
import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class Plain extends Transformation {

	private final AlphaColor color;

	public Plain(LightId lightId, Position start, Duration duration, AlphaColor color) {
		super(lightId, start, duration);
		this.color = requireNonNull(color);
	}

	@Override
	public Transformation with(LightId lightId) {
		return new Plain(lightId, start(), duration(), color);
	}

	@Override
	public Transformation with(Position start) {
		return new Plain(lightId(), start, duration(), color);
	}

	@Override
	public AlphaColor transform(FrameRate rate, Position position) {
		return color;
	}

}
