package de.malkusch.lightshow.renderer.infrastructure;

import java.util.stream.Stream;

import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.Color;
import de.malkusch.lightshow.renderer.model.MixService;

public class NullMixService implements MixService {

	@Override
	public Color mix(Stream<AlphaColor> colors) {
		var any = colors.findAny().get();
		return new Color((byte) (any.color().red() * any.alpha()), (byte) (any.color().green() * any.alpha()),
				(byte) (any.color().blue() * any.alpha()));
	}

}
