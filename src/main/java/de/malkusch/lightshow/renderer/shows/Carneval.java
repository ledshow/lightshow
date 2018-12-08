package de.malkusch.lightshow.renderer.shows;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.common.model.Position;
import de.malkusch.lightshow.renderer.infrastructure.transformation.Fade;
import de.malkusch.lightshow.renderer.infrastructure.transformation.GroupFactory;
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
	private final AlphaColor green = new AlphaColor(new Color(0, 255, 0), 255);

	public Carneval() {
		var lightIds = lights.stream().map(Light::id).collect(Collectors.toList());
		leftToRight = new RunnerFactory(lightIds);
		var reversedIds = new ArrayList<>(lightIds);
		Collections.reverse(reversedIds);
		rightToLeft = new RunnerFactory(reversedIds);
		allLights = new GroupFactory(lightIds);
	}

	public List<Light> lights() {
		return lights;
	}

	public List<Transformation> load(FrameRate frameRate) {
		var highPianoColor = new AlphaColor(new Color(0, 100, 255), 80);
		var highPianoBlink = Fade.blink(leftCenter.id(), new Position(0), highPianoColor, frameRate.duration(0, 100),
				frameRate.duration(0, 200));

		var pianoEnd = frameRate.position(24, 0);

		for (var position = new Position(0); !position.isAfter(pianoEnd); position = position
				.shift(frameRate.duration(0, random(50, 150)))) {
			var seq1 = leftToRight.runner(highPianoBlink.with(position), frameRate.duration(0, random(200, 600)));
			position = position.shift(frameRate.duration(0, 50));
			var seq2 = rightToLeft.runner(highPianoBlink.with(position), frameRate.duration(0, random(200, 600)));
			transformations.addAll(Sequence.from(seq1, seq2).transformations());
		}

		var highLoudPianoColor = highPianoColor.withAlpha(255);
		var loudPianoBlink = Fade.blink(leftCenter.id(), frameRate.position(24, 800), highLoudPianoColor,
				frameRate.duration(0, 150), frameRate.duration(0, 450));
		transformations.addAll(allLights.grouped(loudPianoBlink).transformations());

		var stringColor = red.withAlpha(150);
		var string1 = leftToRight.runner(Fade.blink(leftCenter.id(), frameRate.position(6, 0), stringColor,
				frameRate.duration(0, 100), frameRate.duration(1, 0)), frameRate.duration(0, 500));
		var string2 = rightToLeft.runner(Fade.blink(leftCenter.id(), frameRate.position(8, 500), stringColor,
				frameRate.duration(0, 100), frameRate.duration(1, 0)), frameRate.duration(0, 500));

		transformations.addAll(string1.withStart(frameRate.position(6, 0)).transformations());
		transformations.addAll(string2.withStart(frameRate.position(8, 500)).transformations());
		transformations.addAll(string1.withStart(frameRate.position(10, 800)).transformations());
		transformations.addAll(string2.withStart(frameRate.position(13, 200)).transformations());
		transformations.addAll(string1.withStart(frameRate.position(15, 800)).transformations());

		var string1Short = rightToLeft.runner(Fade.blink(leftCenter.id(), frameRate.position(6, 0), stringColor,
				frameRate.duration(0, 100), frameRate.duration(1, 0)), frameRate.duration(0, 250));
		
		var string2Short = leftToRight.runner(Fade.blink(leftCenter.id(), frameRate.position(6, 0), stringColor,
				frameRate.duration(0, 100), frameRate.duration(1, 0)), frameRate.duration(0, 250));

		transformations.addAll(string1Short.withStart(frameRate.position(17, 900)).transformations());
		transformations.addAll(string2Short.withStart(frameRate.position(18, 700)).transformations());
		transformations.addAll(string1Short.withStart(frameRate.position(19, 700)).transformations());
		transformations.addAll(string2Short.withStart(frameRate.position(20, 750)).transformations());
		transformations.addAll(string1Short.withStart(frameRate.position(21, 900)).transformations());
		transformations.addAll(string2Short.withStart(frameRate.position(23, 0)).transformations());

		return transformations;
	}
	
	

	private final static Random RANDOM = new Random(1);

	private static int random(int min, int max) {
		return RANDOM.nextInt(max - min) + min;
	}

}
