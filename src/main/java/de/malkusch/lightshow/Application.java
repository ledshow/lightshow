package de.malkusch.lightshow;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.player.application.PlayShow;
import de.malkusch.lightshow.player.application.PlayShowApplicationService;
import de.malkusch.lightshow.player.infrastructure.InfrastructureConfiguration;
import de.malkusch.lightshow.renderer.application.RenderShow;
import de.malkusch.lightshow.renderer.application.RenderShowApplicationService;
import de.malkusch.lightshow.renderer.infrastructure.AlphaBlendingMixService;
import de.malkusch.lightshow.renderer.infrastructure.ListLightRepository;
import de.malkusch.lightshow.renderer.infrastructure.transformation.Fade;
import de.malkusch.lightshow.renderer.infrastructure.transformation.RunnerFactory;
import de.malkusch.lightshow.renderer.model.Address;
import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.Color;
import de.malkusch.lightshow.renderer.model.Light;
import de.malkusch.lightshow.renderer.model.LightId;
import de.malkusch.lightshow.renderer.model.Position;
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

		var mixer = new AlphaBlendingMixService();

		var renderer = new RenderService(lights, mixer);
		var renderShowApplicationService = new RenderShowApplicationService(renderer);

		var red = new AlphaColor(new Color(255, 0, 0), 255);
		var green = new AlphaColor(new Color(0, 255, 0), 255);

		var renderShow = new RenderShow();
		renderShow.frameRate = frameRate.framesPerSecond();
		renderShow.transformations = new ArrayList<>();

		var redBlink = Fade.blink(leftCenter.id(), new Position(0), red, frameRate.duration(0, 500),
				frameRate.duration(2, 0));

		var greenBlink = Fade.blink(leftCenter.id(), new Position(0), green, frameRate.duration(0, 500),
				frameRate.duration(2, 0));

		renderShow.transformations.addAll(leftToRight.runner(redBlink, frameRate.position(2, 0)));
		renderShow.transformations
				.addAll(leftToRight.runner(greenBlink.with(frameRate.position(1, 0)), frameRate.position(3, 0)));
		var dmx = renderShowApplicationService.renderShow(renderShow);

		try (audio; dmx) {
			var command = new PlayShow();
			command.audioStream = audio;
			command.dmxStream = dmx;

			playShowApplicationService.playShow(command);
		}
	}

	private static InputStream open(String path) throws MalformedURLException, IOException {
		return new URL(path).openStream();
	}

}
