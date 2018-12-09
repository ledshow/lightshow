package de.malkusch.lightshow.renderer.infrastructure.transformation;

import static de.malkusch.lightshow.renderer.model.Color.BLUE;
import static de.malkusch.lightshow.renderer.model.Color.GREEN;
import static de.malkusch.lightshow.renderer.model.Color.PINK;
import static de.malkusch.lightshow.renderer.model.Color.RED;
import static de.malkusch.lightshow.renderer.model.Color.YELLOW;

import java.util.ArrayList;

import de.malkusch.lightshow.common.model.Duration;
import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.common.model.Position;
import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.Color;
import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class Rainbow extends Transformation {

	private static final Color[] COLORS = { PINK, BLUE, GREEN, YELLOW, RED };
	private final Transformation rainbow;

	public static Sequence running(Position start, Duration duration, RunnerFactory runnerFactory,
			Duration fadeDuration) {

		var rainbow = new Rainbow(LightId.ANY, start, duration);
		var lightCount = runnerFactory.lightIds().size();
		var clipped = new Clip(new Duration(duration.dividedBy(lightCount).frames() - lightCount), rainbow);
		var run = runnerFactory.runner(clipped, duration);
		var fadingRun = run.attachToEach(t -> Fade.fadeout(t, fadeDuration));
		return fadingRun;
	}

	public Rainbow(LightId lightId, Position start, Duration duration) {
		super(lightId, start, duration);

		var steps = COLORS.length - 1;
		var roundedSingleDuration = duration.dividedBy(steps);
		var roundingErrorCompensationDuration = new Duration(
				duration.frames() - (steps - 1) * roundedSingleDuration.frames());
		var lastColor = new AlphaColor(COLORS[0], 255);
		var nextStart = start;
		var fades = new ArrayList<Transformation>();
		for (int i = 1; i < COLORS.length; i++) {
			var nextColor = new AlphaColor(COLORS[i], 255);
			var fadeDuration = i == (COLORS.length - 1) ? roundingErrorCompensationDuration : roundedSingleDuration;
			var fade = Fade.fade(lightId, nextStart, fadeDuration, lastColor, nextColor);
			fades.add(fade);

			lastColor = nextColor;
			nextStart = fade.end().next();
		}
		rainbow = new Composition(fades);
		if (!rainbow.duration().equals(duration)) {
			throw new IllegalStateException(
					String.format("Duration should be %s, but was %s", duration, rainbow.duration()));
		}
	}

	@Override
	public Transformation with(LightId lightId) {
		return new Rainbow(lightId, start(), duration());
	}

	@Override
	public Transformation with(Position start) {
		return new Rainbow(lightId(), start, duration());
	}

	@Override
	public AlphaColor transform(FrameRate rate, Position position) {
		return rainbow.transform(rate, position);
	}

}
