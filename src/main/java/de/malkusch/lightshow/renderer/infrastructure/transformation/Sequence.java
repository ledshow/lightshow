package de.malkusch.lightshow.renderer.infrastructure.transformation;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.malkusch.lightshow.common.model.Duration;
import de.malkusch.lightshow.common.model.Position;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class Sequence {

	private final List<Transformation> transformations;

	public Sequence(Transformation... transformations) {
		this(Arrays.asList(transformations));
	}

	public Sequence(List<? extends Transformation> transformations) {
		if (requireNonNull(transformations).isEmpty()) {
			throw new IllegalArgumentException("Must not be empty");
		}
		this.transformations = Collections.unmodifiableList(transformations);
	}

	public static Sequence from(List<Sequence> sequences) {
		return new Sequence(sequences.stream().flatMap(it -> it.transformations.stream()).collect(Collectors.toList()));
	}

	public static Sequence from(Sequence... sequences) {
		return from(Arrays.asList(sequences));
	}

	public List<Transformation> transformations() {
		return transformations;
	}

	public Position start() {
		return transformations.stream().map(Transformation::start).min(Position::compareTo).get();
	}

	public Position end() {
		return transformations.stream().map(Transformation::end).max(Position::compareTo).get();
	}

	public Duration duration() {
		return new Duration(end().frame() - start().frame() + 1);
	}

	public Sequence withStart(Position start) {
		var delta = start.frame() - start().frame();
		var shifted = transformations.stream().map(it -> it.with(it.start().shift(delta))).collect(Collectors.toList());
		return new Sequence(shifted);
	}

	public Sequence attachToEach(Function<Transformation, Transformation> attach) {
		return new Sequence(transformations.stream().map(attach).toArray(Transformation[]::new));
	}

	public Sequence repeat(int times) {
		if (times <= 0) {
			throw new IllegalArgumentException("Must be greater than zero");
		}

		var sequences = new ArrayList<Sequence>();
		sequences.add(this);
		for (int i = 1; i < times; i++) {
			var last = sequences.get(sequences.size() - 1);
			var next = last.withStart(last.end().next());
			sequences.add(next);
		}
		return from(sequences);
	}
}
