package de.malkusch.lightshow.player.infrastructure;

import de.malkusch.lightshow.player.model.Second;

final class AudioSynchronizer implements Synchronizer {

	private long start;

	@Override
	public void start(Second start) {
		this.start = start.toInt() * 1000;
	}

	@Override
	public long milliseconds(AudioPlayer player) {
		return player.milliseconds() + start;
	}

}
