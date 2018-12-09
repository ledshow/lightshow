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
import de.malkusch.lightshow.renderer.infrastructure.transformation.Filter;
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
		{
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
			var loudPianoBlink = Fade.blink(leftCenter.id(), at(24.75), highLoudPianoColor, duration(0.150),
					duration(0.450));
			add(allLights.grouped(loudPianoBlink));

			var stringColor = red.withAlpha(150);
			var string1 = leftToRight.runner(
					Fade.blink(leftCenter.id(), at(6), stringColor, duration(0.100), duration(1)), duration(0.500));
			var string2 = rightToLeft.runner(
					Fade.blink(leftCenter.id(), at(8.5), stringColor, duration(0.100), duration(1)), duration(0.500));

			add(string1.withStart(at(6)));
			add(string2.withStart(at(8.5)));
			add(string1.withStart(at(10.8)));
			add(string2.withStart(at(13.2)));
			add(string1.withStart(at(15.8)));

			var string1Short = rightToLeft.runner(
					Fade.blink(leftCenter.id(), at(6), stringColor, duration(0.100), duration(1)), duration(0.250));

			var string2Short = leftToRight.runner(
					Fade.blink(leftCenter.id(), at(6), stringColor, duration(0.100), duration(1)), duration(0.250));

			add(string1Short.withStart(at(17.9)));
			add(string2Short.withStart(at(18.7)));
			add(string1Short.withStart(at(19.7)));
			add(string2Short.withStart(at(20.75)));
			add(string1Short.withStart(at(21.9)));
			add(string2Short.withStart(at(23)));

			add(Rainbow.running(at(25.7), duration(0.800), rightToLeft, duration(3)));
		}

		// silence

		{
			var pianoColor = new AlphaColor(new Color(0, 255, 0), 255);
			var rightPiano = Fade.blink(frontRight.id(), at(0), pianoColor, duration(0.05), duration(0.5));
			var rightPianoOpen = Fade.blink(rightPiano.lightId(), at(0), pianoColor, duration(0.05), duration(0.9));
			var darkRightPianoOpen = Filter.darker(rightPianoOpen, 0.5);
			var leftPianoId = frontLeft.id();
			var leftPianoOpen = rightPianoOpen.with(leftPianoId);
			var darkLeftPianoOpen = Filter.darker(leftPianoOpen, 0.5);

			add(rightPiano.with(at(29.450)));
			add(rightPiano.with(at(29.85)));
			add(rightPianoOpen.with(at(30.050)));

			add(rightPiano.with(at(30.55)));
			add(rightPiano.with(at(30.85)));
			add(rightPianoOpen.with(at(31.050)));

			add(withLights(rightPiano.with(at(31.7 - 0.15)), leftPianoId));
			add(withLights(rightPiano.with(at(32.07 - 0.15)), leftPianoId));
			add(withLights(rightPianoOpen.with(at(32.300 - 0.15)), leftPianoId));

			add(withLights(rightPiano.with(at(32.81 - 0.15)), leftPianoId));
			add(withLights(rightPiano.with(at(33.1 - 0.15)), leftPianoId));
			add(withLights(rightPianoOpen.with(at(33.310 - 0.15)), leftPianoId));

			add(withLights(rightPiano.with(at(33.82 - 0.15)), leftPianoId));
			add(withLights(rightPiano.with(at(34.1 - 0.15)), leftPianoId));
			add(withLights(rightPianoOpen.with(at(34.35 - 0.15)), leftPianoId));

			add(leftPianoOpen.with(at(34.85 - 0.15)));
			add(darkRightPianoOpen.with(at(34.85 - 0.15)));

			add(rightPianoOpen.with(at(35.35 - 0.15)));
			add(darkLeftPianoOpen.with(at(35.35 - 0.15)));

			add(leftPianoOpen.with(at(35.87 - 0.15)));
			add(darkRightPianoOpen.with(at(35.87 - 0.15)));

			add(rightPianoOpen.with(at(36.4 - 0.15)));
			add(darkLeftPianoOpen.with(at(36.4 - 0.15)));

			add(leftPianoOpen.with(at(36.93 - 0.15)));
			add(darkRightPianoOpen.with(at(36.93 - 0.15)));

			add(rightPianoOpen.with(at(37.46 - 0.15)));
			add(darkLeftPianoOpen.with(at(37.46 - 0.15)));

			add(leftPianoOpen.with(at(38 - 0.15)));
			add(darkRightPianoOpen.with(at(38 - 0.15)));
		}

		{
			var bassColor = new AlphaColor(Color.RED, 170);
			var darkness = 0.5;
			var bass8Single = Fade.blink(frontCenter.id(), at(0), bassColor, duration(0.1), duration(0.4));
			var bass8 = new Sequence(Filter.darker(bass8Single.with(frontLeft.id()), darkness), bass8Single,
					Filter.darker(bass8Single.with(frontRight.id()), darkness));
			var bass4Single = Fade.blink(bass8Single.lightId(), at(0), bassColor, duration(0.1), duration(0.9));
			var bass4 = new Sequence(Filter.darker(bass4Single.with(frontLeft.id()), darkness), bass4Single,
					Filter.darker(bass4Single.with(frontRight.id()), darkness));
			var bass2Single = Fade.blink(bass8Single.lightId(), at(0), bassColor, duration(0.1), duration(1.8));
			var bass2 = new Sequence(Filter.darker(bass2Single.with(frontLeft.id()), darkness), bass2Single,
					Filter.darker(bass2Single.with(frontRight.id()), darkness));

			var eigth = 0.36;

			var bass = Sequence.from(bass8.withStart(at(38.5)), bass8.withStart(at(38.5 + eigth)),
					bass8.withStart(at(38.5 + 2 * eigth)), bass8.withStart(at(38.5 + 3 * eigth)),
					bass4.withStart(at(38.5 + 4 * eigth)), bass8.withStart(at(38.5 + 6 * eigth)),
					bass8.withStart(at(38.5 + 7 * eigth)), bass8.withStart(at(38.5 + 8 * eigth)),
					bass8.withStart(at(38.5 + 9 * eigth)), bass8.withStart(at(38.5 + 10 * eigth)),
					bass8.withStart(at(38.5 + 11 * eigth)), bass2.withStart(at(38.5 + 12 * eigth)));

			add(bass);
			add(bass.withStart(at(38.5 + 16 * eigth)));
		}

		return transformations;
	}

	private Sequence withLights(Transformation transformation, LightId... ids) {
		var idList = new ArrayList<>(asList(ids));
		idList.add(transformation.lightId());
		var groups = new GroupFactory(idList);
		return groups.grouped(transformation);
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

	private void add(Transformation transformation) {
		transformations.add(transformation);
	}

	private void add(Sequence sequence) {
		transformations.addAll(sequence.transformations());
	}

	private final static Random RANDOM = new Random(1);

	private static int random(int min, int max) {
		return RANDOM.nextInt(max - min) + min;
	}

}
