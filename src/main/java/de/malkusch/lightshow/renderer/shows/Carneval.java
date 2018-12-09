package de.malkusch.lightshow.renderer.shows;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import de.malkusch.lightshow.common.model.Duration;
import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.common.model.Position;
import de.malkusch.lightshow.renderer.infrastructure.transformation.Fade;
import de.malkusch.lightshow.renderer.infrastructure.transformation.GroupFactory;
import de.malkusch.lightshow.renderer.infrastructure.transformation.Rainbow;
import de.malkusch.lightshow.renderer.infrastructure.transformation.RunnerFactory;
import de.malkusch.lightshow.renderer.infrastructure.transformation.Sequence;
import de.malkusch.lightshow.renderer.model.Address;
import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.Color;
import de.malkusch.lightshow.renderer.model.Light;
import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Transformation;

public final class Carneval {

	private final List<Transformation> transformations = new ArrayList<>();
	private final Light leftCenter = new Light(new LightId("leftCenter"), new Address(0));
	private final Light leftFront = new Light(new LightId("leftFront"), new Address(3));
	private final Light frontLeft = new Light(new LightId("frontLeft"), new Address(6));
	private final Light frontCenter = new Light(new LightId("frontCenter"), new Address(9));
	private final Light frontRight = new Light(new LightId("frontRight"), new Address(12));
	private final Light rightFront = new Light(new LightId("rightFront"), new Address(15));
	private final Light rightCenter = new Light(new LightId("rightCenter"), new Address(18));
	private final List<Light> lights = asList(leftCenter, leftFront, frontLeft, frontCenter, frontRight, rightFront,
			rightCenter);
	private final RunnerFactory leftToRight;
	private final RunnerFactory rightToLeft;
	private final GroupFactory allLights;
	private final AlphaColor red = new AlphaColor(new Color(255, 0, 0), 255);
	private final FrameRate frameRate;

	public Carneval(FrameRate frameRate) {
		var lightIds = lights.stream().map(Light::id).collect(Collectors.toList());
		leftToRight = new RunnerFactory(lightIds);
		var reversedIds = new ArrayList<>(lightIds);
		Collections.reverse(reversedIds);
		rightToLeft = new RunnerFactory(reversedIds);
		allLights = new GroupFactory(lightIds);
		this.frameRate = frameRate;
	}

	public List<Light> lights() {
		return lights;
	}

	public List<Transformation> load() {
		var highPianoColor = new AlphaColor(new Color(0, 100, 255), 80);
		var highPianoBlink = Fade.blink(leftCenter.id(), at(0), highPianoColor, duration(0.1), duration(0.2));

		var pianoEnd = at(24);

		for (var position = at(0); !position.isAfter(pianoEnd); position = position
				.shift(frameRate.duration(0, random(50, 150)))) {
			var seq1 = leftToRight.runner(highPianoBlink.with(position), frameRate.duration(0, random(200, 600)));
			position = position.shift(duration(0.050));
			var seq2 = rightToLeft.runner(highPianoBlink.with(position), frameRate.duration(0, random(200, 600)));
			add(Sequence.from(seq1, seq2));
		}

		var highLoudPianoColor = highPianoColor.withAlpha(255);
		var loudPianoBlink = Fade.blink(leftCenter.id(), at(24.8), highLoudPianoColor, duration(0.150),
				duration(0.450));
		add(allLights.grouped(loudPianoBlink));

		var stringColor = red.withAlpha(150);
		var string1 = leftToRight.runner(Fade.blink(leftCenter.id(), at(6), stringColor, duration(0.100), duration(1)),
				duration(0.500));
		var string2 = rightToLeft.runner(
				Fade.blink(leftCenter.id(), at(8.5), stringColor, duration(0.100), duration(1)), duration(0.500));

		add(string1.withStart(at(6)));
		add(string2.withStart(at(8.5)));
		add(string1.withStart(at(10.8)));
		add(string2.withStart(at(13.2)));
		add(string1.withStart(at(15.8)));

		var string1Short = rightToLeft
				.runner(Fade.blink(leftCenter.id(), at(6), stringColor, duration(0.100), duration(1)), duration(0.250));

		var string2Short = leftToRight
				.runner(Fade.blink(leftCenter.id(), at(6), stringColor, duration(0.100), duration(1)), duration(0.250));

		add(string1Short.withStart(at(17.9)));
		add(string2Short.withStart(at(18.7)));
		add(string1Short.withStart(at(19.7)));
		add(string2Short.withStart(at(20.75)));
		add(string1Short.withStart(at(21.9)));
		add(string2Short.withStart(at(23)));

		add(Rainbow.running(frameRate.position(25, 900), duration(0.700), rightToLeft, duration(3)));

		return transformations;
	}

	private Duration duration(int seconds) {
		return frameRate.duration(seconds, 0);
	}

	private Duration duration(double seconds) {
		var intSeconds = (int) seconds;
		var milliseconds = (int) Math.round((seconds - intSeconds) * 1000);
		if (milliseconds < 0) {
			milliseconds = 0;
		}
		return frameRate.duration(intSeconds, milliseconds);
	}

	private Position at(int second) {
		return frameRate.position(second, 0);
	}

	private Position at(double second) {
		var intSecond = (int) second;
		var millisecond = (int) Math.round((second - intSecond) * 1000);
		if (millisecond < 0) {
			millisecond = 0;
		}
		return frameRate.position(intSecond, millisecond);
	}

	private void add(Sequence sequence) {
		transformations.addAll(sequence.transformations());
	}

	private final static Random RANDOM = new Random(1);

	private static int random(int min, int max) {
		return RANDOM.nextInt(max - min) + min;
	}

}
