package de.malkusch.lightshow.renderer.infrastructure.transformation;

import static java.util.Arrays.stream;

import java.util.Arrays;

import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.Duration;
import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Position;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class Composition extends Transformation {

	private Transformation[] transformations;

	public Composition(Transformation... transformations) {
		super(assertSameLightId(transformations), start(transformations), duration(transformations));
		this.transformations = transformations;
	}

	private static LightId assertSameLightId(Transformation[] transformations) {
		var count = stream(transformations).map(Transformation::lightId).distinct().count();
		if (count != 1) {
			throw new IllegalArgumentException("Must be identical light id");
		}
		return transformations[0].lightId();
	}

	private static Duration duration(Transformation[] transformations) {
		return stream(transformations).map(Transformation::duration).reduce(Duration::add).get();
	}

	private static Position start(Transformation[] transformations) {
		return stream(transformations).map(t -> t.start().frame()).min(Long::compare).map(Position::new).get();
	}

	@Override
	public AlphaColor transform(FrameRate rate, Position position) {
		var active = Arrays.stream(transformations).filter(t -> t.isActive(position)).findFirst().get();
		return active.transform(rate, position);
	}

}
