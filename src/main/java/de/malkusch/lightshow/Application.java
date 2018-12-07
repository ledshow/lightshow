package de.malkusch.lightshow;

import static java.util.Arrays.asList;

import java.io.BufferedInputStream;
import java.io.InputStream;

import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.player.application.PlayShow;
import de.malkusch.lightshow.player.application.PlayShowApplicationService;
import de.malkusch.lightshow.player.infrastructure.InfrastructureConfiguration;
import de.malkusch.lightshow.renderer.application.RenderShow;
import de.malkusch.lightshow.renderer.application.RenderShowApplicationService;
import de.malkusch.lightshow.renderer.infrastructure.ListLightRepository;
import de.malkusch.lightshow.renderer.infrastructure.NullMixService;
import de.malkusch.lightshow.renderer.infrastructure.transformation.Strobe;
import de.malkusch.lightshow.renderer.model.Address;
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
		var audio = open("/carneval.wav");
		var frequency = 1;
		var center = new Light(new LightId("center"), new Address(0), new Address(1), new Address(2));
		var mixer = new NullMixService();
		var lights = new ListLightRepository(asList(center));
		var renderer = new RenderService(lights, mixer);
		var renderShowApplicationService = new RenderShowApplicationService(renderer);
		var white = new Color(255, 255, 255);
		var strobe = new Strobe(center.id(), new Position(0), frameRate.position(60, 0), frequency, white);

		var renderShow = new RenderShow();
		renderShow.frameRate = frameRate.framesPerSecond();
		renderShow.transformations = asList(strobe);
		var dmx = renderShowApplicationService.renderShow(renderShow);

		try (audio; dmx) {
			var command = new PlayShow();
			command.audioStream = audio;
			command.dmxStream = dmx;

			playShowApplicationService.playShow(command);
		}
	}

	private static InputStream open(String path) {
		return new BufferedInputStream(Application.class.getResourceAsStream(path));
	}

}
