package de.malkusch.lightshow.renderer.model;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toUnmodifiableList;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import de.malkusch.lightshow.common.model.Position;

public final class Show {

	public Show(List<Transformation> transformations) {
		if (requireNonNull(transformations).isEmpty()) {
			throw new IllegalArgumentException("Must not be empty");
		}
		this.transformations = transformations;
		lightIds = transformations.stream().map(Transformation::lightId).distinct().collect(toUnmodifiableList());
		end = transformations.stream().map(Transformation::end).max(Comparator.comparingLong(Position::frame)).get();
	}

	private final List<LightId> lightIds;

	public List<LightId> lightIds() {
		return lightIds;
	}

	private final List<Transformation> transformations;

	public Stream<Transformation> transformations(LightId lightId, Position position) {
		requireNonNull(lightId);
		requireNonNull(position);

		return transformations.stream().filter(t -> t.lightId().equals(lightId) && t.isActive(position));
	}

	private final Position end;

	public Position end() {
		return end;
	}
}
