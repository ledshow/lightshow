package de.malkusch.lightshow.renderer.model;

import java.util.stream.Stream;

public interface MixService {

	Color mix(Stream<AlphaColor> color);

}
