package de.malkusch.lightshow;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.player.application.PlayShow;
import de.malkusch.lightshow.player.application.PlayShowApplicationService;
import de.malkusch.lightshow.player.infrastructure.InfrastructureConfiguration;
import de.malkusch.lightshow.renderer.application.RenderShow;
import de.malkusch.lightshow.renderer.application.RenderShowApplicationService;
import de.malkusch.lightshow.renderer.infrastructure.AlphaBlendingMixService;
import de.malkusch.lightshow.renderer.infrastructure.ListLightRepository;
import de.malkusch.lightshow.renderer.model.RenderService;
import de.malkusch.lightshow.renderer.shows.Carneval;

public final class Application {

	public static void main(String[] args) throws Exception {

		var frameRate = new FrameRate(120);
		var show = new Carneval(frameRate);

		var bufferFrames = 5000;
		var infrastructure = new InfrastructureConfiguration(bufferFrames, frameRate);
		var audio = open("file://" + args[0]);

		var lights = new ListLightRepository(show.lights());
		var mixer = new AlphaBlendingMixService();
		var renderer = new RenderService(lights, mixer);
		var renderShowApplicationService = new RenderShowApplicationService(renderer);

		var renderShow = new RenderShow();
		renderShow.frameRate = frameRate.framesPerSecond();
		renderShow.transformations = show.load();

		var dmx = renderShowApplicationService.renderShow(renderShow);

		var playShowService = infrastructure.playShowService();
		var playShowApplicationService = new PlayShowApplicationService(playShowService);
		try (audio; dmx; playShowService) {
			var command = new PlayShow();
			command.audioStream = audio;
			command.dmxStream = dmx;
			command.start = 0;

			playShowApplicationService.playShow(command);
		}
	}

	private static InputStream open(String path) throws MalformedURLException, IOException {
		return new URL(path).openStream();
	}

}
