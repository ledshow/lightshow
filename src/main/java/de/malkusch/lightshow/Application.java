package de.malkusch.lightshow;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.malkusch.lightshow.common.infrastructure.TestDmxStream;
import de.malkusch.lightshow.common.model.FrameRate;
import de.malkusch.lightshow.player.application.PlayShow;
import de.malkusch.lightshow.player.application.PlayShowApplicationService;
import de.malkusch.lightshow.player.infrastructure.InfrastructureConfiguration;

public final class Application {

	public static void main(String[] args) throws IOException, InterruptedException {
		var bufferFrames = 5000;
		var frameRate = new FrameRate(120);
		var infrastructure = new InfrastructureConfiguration(bufferFrames, frameRate);
		var playShowApplicationService = new PlayShowApplicationService(infrastructure.playShowService());
		var audio = open("/carneval.wav");
		var frequency = 30;
		var channel = 3;
		var dmx = new TestDmxStream(channel, frequency);

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
