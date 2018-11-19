package de.malkusch.lightshow.player.infrastructure;

interface DmxPlayer {

	void startPlayback();

	boolean hasFrames();

	void stop();

	void synchronizeMilliseconds(long milliseconds);

}
