package de.malkusch.lightshow.player.infrastructure;

interface AudioPlayer extends AutoCloseable {

	void startPlayback();

	boolean hasFrames();

	long milliseconds();

	void stop();

}
