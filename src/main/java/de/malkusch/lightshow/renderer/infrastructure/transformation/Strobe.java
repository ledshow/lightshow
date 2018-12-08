package de.malkusch.lightshow.renderer.infrastructure.transformation;

import static java.util.Objects.requireNonNull;

import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.Duration;
import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Position;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class Strobe extends Transformation {

	private final int frequency;
	private final AlphaColor color;

	public Strobe(LightId lightId, Position start, Duration duration, int frequency, AlphaColor color) {
		super(lightId, start, duration);
		this.frequency = assertValidFrequency(frequency);
		this.color = requireNonNull(color);
	}

	private static int assertValidFrequency(int frequency) {
		if (frequency <= 0) {
			throw new IllegalArgumentException("Frequency must be positive");
		}
		return frequency;
	}

	@Override
	public Transformation with(LightId lightId) {
		return new Strobe(lightId, start(), duration(), frequency, color);
	}

	@Override
	public Transformation with(Position start) {
		return new Strobe(lightId(), start, duration(), frequency, color);
	}

	@Override
	public AlphaColor transform(FrameRate rate, Position position) {
		var relativePosition = relativePosition(position).frame();
		var period = rate.framesPerSecond() / frequency;
		var alpha = isEven(relativePosition / (period / 2)) ? 0 : color.alpha();
		return color.withAlpha(alpha);
	}

	private static boolean isEven(long value) {
		return (value & 1) == 0;
	}

}
