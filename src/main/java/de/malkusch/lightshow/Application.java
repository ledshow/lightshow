package de.malkusch.lightshow;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.common.model.Position;
import de.malkusch.lightshow.player.application.PlayShow;
import de.malkusch.lightshow.player.application.PlayShowApplicationService;
import de.malkusch.lightshow.player.infrastructure.InfrastructureConfiguration;
import de.malkusch.lightshow.renderer.application.RenderShow;
import de.malkusch.lightshow.renderer.application.RenderShowApplicationService;
import de.malkusch.lightshow.renderer.infrastructure.AlphaBlendingMixService;
import de.malkusch.lightshow.renderer.infrastructure.ListLightRepository;
import de.malkusch.lightshow.renderer.infrastructure.transformation.Fade;
import de.malkusch.lightshow.renderer.infrastructure.transformation.GroupFactory;
import de.malkusch.lightshow.renderer.infrastructure.transformation.RunnerFactory;
import de.malkusch.lightshow.renderer.infrastructure.transformation.Sequence;
import de.malkusch.lightshow.renderer.model.Address;
import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.Color;
import de.malkusch.lightshow.renderer.model.Light;
import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.RenderService;

public final class Application {

	public static void main(String[] args) throws Exception {
		var bufferFrames = 5000;
		var frameRate = new FrameRate(120);
		var infrastructure = new InfrastructureConfiguration(bufferFrames, frameRate);
		var playShowApplicationService = new PlayShowApplicationService(infrastructure.playShowService());
		var audio = open("file://" + args[0]);

		var leftCenter = new Light(new LightId("leftCenter"), new Address(0));
		var leftFront = new Light(new LightId("leftFront"), new Address(3));
		var frontLeft = new Light(new LightId("frontLeft"), new Address(6));
		var frontCenter = new Light(new LightId("frontCenter"), new Address(9));
		var frontRight = new Light(new LightId("frontRight"), new Address(12));
		var rightFront = new Light(new LightId("rightFront"), new Address(15));
		var rightCenter = new Light(new LightId("rightCenter"), new Address(18));
		var lights = new ListLightRepository(
				asList(leftCenter, leftFront, frontLeft, frontCenter, frontRight, rightFront, rightCenter));
		var lightIds = lights.findAll().stream().map(Light::id).collect(Collectors.toList());

		var leftToRight = new RunnerFactory(lightIds);
		var reversedIds = new ArrayList<>(lightIds);
		Collections.reverse(reversedIds);
		var rightToLeft = new RunnerFactory(reversedIds);
		var allLights = new GroupFactory(lightIds);

		var mixer = new AlphaBlendingMixService();

		var renderer = new RenderService(lights, mixer);
		var renderShowApplicationService = new RenderShowApplicationService(renderer);

		var red = new AlphaColor(new Color(255, 0, 0), 255);
		var green = new AlphaColor(new Color(0, 255, 0), 255);

		var renderShow = new RenderShow();
		renderShow.frameRate = frameRate.framesPerSecond();
		renderShow.transformations = new ArrayList<>();

		var highPianoColor = green.withAlpha(80);
		var highPianoBlink = Fade.blink(leftCenter.id(), new Position(0), highPianoColor, frameRate.duration(0, 100),
				frameRate.duration(0, 200));

		var pianoEnd = frameRate.position(24, 0);

		for (var position = new Position(0); !position.isAfter(pianoEnd); position = position
				.shift(frameRate.duration(0, random(50, 150)))) {
			var seq1 = leftToRight.runner(highPianoBlink.with(position), frameRate.duration(0, random(200, 600)));
			position = position.shift(frameRate.duration(0, 50));
			var seq2 = rightToLeft.runner(highPianoBlink.with(position), frameRate.duration(0, random(200, 600)));
			renderShow.transformations.addAll(Sequence.from(seq1, seq2).transformations());
		}

		var highLoudPianoColor = highPianoColor.withAlpha(200);
		var loudPianoBlink = Fade.blink(leftCenter.id(), frameRate.position(24, 800), highLoudPianoColor,
				frameRate.duration(0, 150), frameRate.duration(0, 200));
		renderShow.transformations.addAll(allLights.grouped(loudPianoBlink).transformations());

		var stringColor = red.withAlpha(150);
		var string1 = leftToRight.runner(Fade.blink(leftCenter.id(), frameRate.position(6, 0), stringColor,
				frameRate.duration(0, 100), frameRate.duration(1, 0)), frameRate.duration(0, 500));
		var string2 = rightToLeft.runner(Fade.blink(leftCenter.id(), frameRate.position(8, 500), stringColor,
				frameRate.duration(0, 100), frameRate.duration(1, 0)), frameRate.duration(0, 500));

		renderShow.transformations.addAll(string1.withStart(frameRate.position(6, 0)).transformations());
		renderShow.transformations.addAll(string2.withStart(frameRate.position(8, 500)).transformations());
		renderShow.transformations.addAll(string1.withStart(frameRate.position(10, 500)).transformations());
		renderShow.transformations.addAll(string2.withStart(frameRate.position(13, 000)).transformations());
		renderShow.transformations.addAll(string1.withStart(frameRate.position(15, 500)).transformations());

		var dmx = renderShowApplicationService.renderShow(renderShow);

		try (audio; dmx) {
			var command = new PlayShow();
			command.audioStream = audio;
			command.dmxStream = dmx;
			command.start = 22;

			playShowApplicationService.playShow(command);
		}
	}

	private final static Random RANDOM = new Random(1);

	private static int random(int min, int max) {
		return RANDOM.nextInt(max - min) + min;
	}

	private static InputStream open(String path) throws MalformedURLException, IOException {
		return new URL(path).openStream();
	}

}
