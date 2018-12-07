package de.malkusch.lightshow.renderer.infrastructure;

import java.util.function.Function;
import java.util.stream.Stream;

import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.Color;
import de.malkusch.lightshow.renderer.model.MixService;

public class NullMixService implements MixService {

	@Override
	public Color mix(Stream<AlphaColor> colors) {
		var any = colors.findAny().get();
		return new Color(mix(Color::red, any), mix(Color::green, any), mix(Color::blue, any));
	}

	private static int mix(Function<Color, Integer> getColor, AlphaColor color) {
		return getColor.apply(color.color()).intValue() * color.alpha() / 255;
	}

}
