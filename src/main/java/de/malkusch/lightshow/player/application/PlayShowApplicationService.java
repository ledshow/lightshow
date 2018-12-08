package de.malkusch.lightshow.player.application;

import java.io.IOException;

import de.malkusch.lightshow.player.model.AudioStream;
import de.malkusch.lightshow.player.model.PlayShowService;
import de.malkusch.lightshow.player.model.Second;
import de.malkusch.lightshow.player.model.Show;

public final class PlayShowApplicationService {

	private final PlayShowService player;

	public PlayShowApplicationService(PlayShowService player) {
		this.player = player;
	}

	public void playShow(PlayShow command) throws InterruptedException, IOException {
		AudioStream audioStream = new AudioStream(command.audioStream);
		var dmxStream = command.dmxStream;
		var start = new Second(command.start);
		Show show = new Show(audioStream, dmxStream);
		player.play(show, start);
	}

}
