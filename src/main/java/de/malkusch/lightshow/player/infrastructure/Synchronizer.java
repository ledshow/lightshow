package de.malkusch.lightshow.player.infrastructure;

import de.malkusch.lightshow.player.model.Second;

interface Synchronizer {

	void start(Second start);

	long milliseconds(AudioPlayer player);

}
