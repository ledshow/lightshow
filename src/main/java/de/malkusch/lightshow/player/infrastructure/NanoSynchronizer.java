package de.malkusch.lightshow.player.infrastructure;

public class NanoSynchronizer implements Synchronizer {

	private long start;

	@Override
	public void start() {
		start = System.nanoTime();
	}

	@Override
	public long milliseconds(AudioPlayer player) {
		return (System.nanoTime() - start) / 1000000;
	}

}
