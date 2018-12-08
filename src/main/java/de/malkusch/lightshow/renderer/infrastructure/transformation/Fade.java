package de.malkusch.lightshow.renderer.infrastructure.transformation;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import de.malkusch.lightshow.common.model.Duration;
import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.common.model.Position;
import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.Color;
import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class Fade extends Transformation {

	public Fade(LightId lightId, Position start, Duration duration, AlphaColor from, AlphaColor to) {
		super(lightId, start, duration);

		this.from = requireNonNull(from);
		this.to = requireNonNull(to);
	}

	@Override
	public Transformation with(LightId lightId) {
		return new Fade(lightId, start(), duration(), from, to);
	}

	@Override
	public Transformation with(Position start) {
		return new Fade(lightId(), start, duration(), from, to);
	}

	public static Transformation blink(LightId lightId, Position start, AlphaColor color, Duration fadeIn,
			Duration fadeOut) {

		var in = fadein(lightId, start, fadeIn, color);
		var out = fadeout(lightId, in.end().next(), fadeOut, color);
		return new Composition(in, out);
	}

	public static Transformation blink(LightId lightId, Position start, AlphaColor color, Duration duration) {
		var fadeIn = duration.dividedBy(2);
		var fadeOut = new Duration(duration.frames() - fadeIn.frames());
		return blink(lightId, start, color, fadeIn, fadeOut);
	}

	public static Transformation fadein(LightId lightId, Position start, Duration duration, AlphaColor color) {
		return new Fade(lightId, start, duration, color.withAlpha(0), color);
	}

	public static Transformation fadeout(LightId lightId, Position start, Duration duration, AlphaColor color) {
		return new Fade(lightId, start, duration, color, color.withAlpha(0));
	}

	private final AlphaColor from;
	private final AlphaColor to;

	@Override
	public AlphaColor transform(FrameRate rate, Position position) {
		var red = transform(rate, position, (i) -> i.color().red());
		var green = transform(rate, position, (i) -> i.color().green());
		var blue = transform(rate, position, (i) -> i.color().blue());
		var alpha = transform(rate, position, AlphaColor::alpha);
		return new AlphaColor(new Color(red, green, blue), alpha);
	}

	private int transform(FrameRate rate, Position position, Function<AlphaColor, Integer> channel) {
		var relativePosition = relativePosition(position);
		var progress = (relativePosition.frame() + 1) / (double) duration().frames();
		var difference = channel.apply(to) - channel.apply(from);
		return channel.apply(from) + (int) (smoothProgress(progress) * difference);
	}

	private static double smoothProgress(double progress) {
		return (Math.cos(Math.PI + Math.PI * progress) + 1) / 2;
	}

}
