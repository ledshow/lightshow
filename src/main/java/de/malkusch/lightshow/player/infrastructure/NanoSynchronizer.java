package de.malkusch.lightshow.player.infrastructure;

import de.malkusch.lightshow.player.model.Second;

public class NanoSynchronizer implements Synchronizer {

	private long start;

	@Override
	public void start(Second start) {
		this.start = System.nanoTime() - ((long) start.toInt()) * 1000000000;
	}

	@Override
	public long milliseconds(AudioPlayer player) {
		return (System.nanoTime() - start) / 1000000;
	}

}
