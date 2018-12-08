package de.malkusch.lightshow.player.model;

import java.io.IOException;

public interface PlayShowService {

	void play(Show show, Second start) throws InterruptedException, IOException;

}
