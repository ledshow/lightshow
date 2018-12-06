package de.malkusch.lightshow.renderer.application;

import java.util.stream.Stream;

import de.malkusch.lightshow.common.infrastructure.JavaStreamDmxStream;
import de.malkusch.lightshow.common.model.DmxStream;
import de.malkusch.lightshow.renderer.model.Position;
import de.malkusch.lightshow.renderer.model.RenderService;
import de.malkusch.lightshow.renderer.model.Show;

public final class RenderShowApplicationService {

	private final RenderService renderer;

	public RenderShowApplicationService(RenderService renderer) {
		this.renderer = renderer;
	}

	public DmxStream renderShow(RenderShow command) {
		var show = new Show(command.transformations);
		var start = new Position(0);

		var frames = Stream.iterate(start, p -> p.isWithin(start, show.end()), Position::next)
				.map(it -> renderer.render(show, it));
		return new JavaStreamDmxStream(frames);
	}

}
