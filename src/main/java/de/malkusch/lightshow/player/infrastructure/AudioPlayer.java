package de.malkusch.lightshow.player.infrastructure;

import java.io.IOException;

interface AudioPlayer extends AutoCloseable {

	void startPlayback();

	boolean hasFrames();

	void fillBuffer() throws IOException;

	long milliseconds();

	void stop();

}
