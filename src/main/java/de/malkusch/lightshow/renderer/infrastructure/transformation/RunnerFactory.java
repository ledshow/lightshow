package de.malkusch.lightshow.renderer.infrastructure.transformation;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Position;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class RunnerFactory {

	private final List<LightId> lightIds;

	public RunnerFactory(List<LightId> lightIds) {
		if (requireNonNull(lightIds).isEmpty()) {
			throw new IllegalArgumentException("Must not be empty");
		}
		this.lightIds = lightIds;
	}

	public List<Transformation> runner(Transformation first, Position last) {
		var step = Math.round((last.frame() - first.start().frame()) / (double) lightIds.size());
		List<Transformation> runner = new ArrayList<>(lightIds.size());
		for (int i = 0; i < lightIds.size(); i++) {
			var position = new Position(first.start().frame() + i * step);
			var lightId = lightIds.get(i);
			var shifted = first.with(position).with(lightId);
			runner.add(shifted);
		}
		return runner;
	}

}
