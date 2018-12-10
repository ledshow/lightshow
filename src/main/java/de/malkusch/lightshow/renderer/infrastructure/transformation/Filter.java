package de.malkusch.lightshow.renderer.infrastructure.transformation;

import java.util.function.Function;

import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.common.model.Position;
import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class Filter extends Transformation {

	public static Filter darker(Transformation transformation, double darkness) {
		return new Filter(transformation, darkness(darkness));
	}

	public static Sequence darker(Sequence sequence, double darkness) {
		return filter(sequence, darkness(darkness));
	}

	private static Function<AlphaColor, AlphaColor> darkness(double darkness) {
		if (!(darkness >= 0 && darkness <= 1)) {
			throw new IllegalArgumentException("Must be within 1 and 0");
		}
		return c -> c.withAlpha((int) Math.round(c.alpha() * darkness));
	}

	public static Sequence filter(Sequence sequence, Function<AlphaColor, AlphaColor> filter) {
		var filtered = sequence.transformations().stream().map(it -> new Filter(it, filter))
				.toArray(Transformation[]::new);
		return new Sequence(filtered);
	}

	private final Transformation transformation;
	private final Function<AlphaColor, AlphaColor> filter;

	public Filter(Transformation transformation, Function<AlphaColor, AlphaColor> filter) {
		super(transformation.lightId(), transformation.start(), transformation.duration());
		this.transformation = transformation;
		this.filter = filter;
	}

	@Override
	public Transformation with(LightId lightId) {
		return new Filter(transformation.with(lightId), filter);
	}

	@Override
	public Transformation with(Position start) {
		return new Filter(transformation.with(start), filter);
	}

	@Override
	public AlphaColor transform(FrameRate rate, Position position) {
		return filter.apply(transformation.transform(rate, position));
	}

}
