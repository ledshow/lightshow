package de.malkusch.lightshow.player.infrastructure;

final class AudioSynchronizer implements Synchronizer {

	@Override
	public long milliseconds(AudioPlayer player) {
		return player.milliseconds();
	}

	@Override
	public void start() {
	}

}
