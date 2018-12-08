package de.malkusch.lightshow.renderer.infrastructure.transformation;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.stream.Collectors;

import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class GroupFactory {

	private final List<LightId> lightIds;

	public GroupFactory(List<LightId> lightIds) {
		if (requireNonNull(lightIds).isEmpty()) {
			throw new IllegalArgumentException("Must not be empty");
		}
		this.lightIds = lightIds;
	}

	public Sequence grouped(Transformation transformation) {
		requireNonNull(transformation);
		return new Sequence(lightIds.stream().map(transformation::with).collect(Collectors.toList()));
	}

}
