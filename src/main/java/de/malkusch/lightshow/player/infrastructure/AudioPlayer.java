package de.malkusch.lightshow.player.infrastructure;

import java.io.IOException;

import de.malkusch.lightshow.player.model.Second;

interface AudioPlayer extends AutoCloseable {

	void startPlayback(Second start) throws IOException;

	boolean hasFrames();

	long milliseconds();

	void stop();

}
