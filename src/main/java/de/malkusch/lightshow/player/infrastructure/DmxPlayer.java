package de.malkusch.lightshow.player.infrastructure;

import java.io.IOException;

interface DmxPlayer {

	void startPlayback();

	boolean hasFrames();

	void stop();

	void render(long milliseconds) throws IOException;

}
