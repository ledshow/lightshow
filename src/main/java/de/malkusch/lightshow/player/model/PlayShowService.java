package de.malkusch.lightshow.player.model;

import java.io.IOException;

public interface PlayShowService extends AutoCloseable {

	void play(Show show, Second start) throws InterruptedException, IOException;

}
