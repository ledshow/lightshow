package de.malkusch.lightshow.player.infrastructure;

import java.io.IOException;

import de.malkusch.lightshow.player.model.Second;

interface DmxPlayer {

	void startPlayback(Second start);

	boolean hasFrames();

	void stop();

	void render(long milliseconds) throws IOException;

}
