package de.malkusch.lightshow.renderer.infrastructure.transformation;

import static java.util.Objects.requireNonNull;

import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.Color;
import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Position;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class Strobe extends Transformation {

	private final int frequency;
	private final Color color;

	public Strobe(LightId lightId, Position start, Position end, int frequency, Color color) {
		super(lightId, start, end);
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
	public AlphaColor transform(FrameRate rate, Position position) {
		var relativePosition = relativePosition(position).frame();
		var period = rate.framesPerSecond() / frequency;
		var alpha = isEven(relativePosition / (period / 2)) ? 0 : 255;
		return new AlphaColor(color, alpha);
	}

	private static boolean isEven(long value) {
		return (value & 1) == 0;
	}

}
