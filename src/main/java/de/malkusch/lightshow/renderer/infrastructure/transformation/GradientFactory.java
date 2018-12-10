package de.malkusch.lightshow.renderer.infrastructure.transformation;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.common.model.Position;
import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.Color;
import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class GradientFactory {

	private final List<LightId> lightIds;

	public GradientFactory(LightId... lightIds) {
		this(Arrays.asList(lightIds));
	}

	public GradientFactory(List<LightId> lightIds) {
		if (requireNonNull(lightIds).isEmpty()) {
			throw new IllegalArgumentException("Must not be empty");
		}
		this.lightIds = lightIds;
	}

	public Sequence gradient(Transformation from, Transformation to) {
		requireNonNull(from);
		requireNonNull(to);

		var gradients = new ArrayList<Transformation>();
		for (int i = 0; i < lightIds.size(); i++) {
			var progress = (double) i / (lightIds.size() - 1);
			var lightId = lightIds.get(i);
			var gradient = new Gradient(progress, from, to).with(lightId);
			gradients.add(gradient);
		}
		return new Sequence(gradients);
	}

	private static class Gradient extends Transformation {

		private final Transformation from, to;
		private final double progress;

		private Gradient(double progress, Transformation from, Transformation to) {
			super(from.lightId(), from.start(), from.duration());

			if (!from.lightId().equals(to.lightId())) {
				throw new IllegalArgumentException("Must be identical light id");
			}
			if (!from.start().equals(to.start())) {
				throw new IllegalArgumentException("Must be identical start");
			}
			if (!from.duration().equals(to.duration())) {
				throw new IllegalArgumentException("Must be identical duration");
			}
			if (!(progress >= 0 && progress <= 1)) {
				throw new IllegalArgumentException("Progress must be within 0 and 1");
			}
			this.from = from;
			this.to = to;
			this.progress = progress;
		}

		@Override
		public Transformation with(LightId lightId) {
			return new Gradient(progress, from.with(lightId), to.with(lightId));
		}

		@Override
		public Transformation with(Position start) {
			return new Gradient(progress, from.with(start), to.with(start));
		}

		@Override
		public AlphaColor transform(FrameRate rate, Position position) {
			var red = transform(rate, position, (i) -> i.color().red());
			var green = transform(rate, position, (i) -> i.color().green());
			var blue = transform(rate, position, (i) -> i.color().blue());
			var alpha = transform(rate, position, AlphaColor::alpha);
			return new AlphaColor(new Color(red, green, blue), alpha);
		}

		private int transform(FrameRate rate, Position position, Function<AlphaColor, Integer> channel) {
			var fromColor = from.transform(rate, position);
			var toColor = to.transform(rate, position);
			var difference = channel.apply(toColor) - channel.apply(fromColor);
			return channel.apply(fromColor) + (int) Math.round((progress) * difference);
		}
	}
}
