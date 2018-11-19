package de.malkusch.lightshow.player;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import de.malkusch.lightshow.player.application.PlayShow;
import de.malkusch.lightshow.player.application.PlayShowApplicationService;
import de.malkusch.lightshow.player.infrastructure.FrameRate;
import de.malkusch.lightshow.player.infrastructure.InfrastructureConfiguration;

public final class Player {

	public static void main(String[] args) throws IOException, InterruptedException {
		int bufferFrames = 5000;
		FrameRate frameRate = new FrameRate(60);
		InfrastructureConfiguration infrastructure = new InfrastructureConfiguration(bufferFrames, frameRate);
		PlayShowApplicationService playShowApplicationService = new PlayShowApplicationService(
				infrastructure.playShowService());

		try (InputStream audio = open("/carneval.wav"); InputStream dmx = open("/carneval.wav")) {
			PlayShow command = new PlayShow();
			command.audioStream = audio;
			command.dmxStream = dmx;

			playShowApplicationService.playShow(command);
		}
	}

	private static InputStream open(String path) {
		return new BufferedInputStream(Player.class.getResourceAsStream(path));
	}

}
