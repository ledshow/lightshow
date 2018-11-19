package de.malkusch.lightshow.player.infrastructure;

interface Synchronizer {

	void start();

	long milliseconds(AudioPlayer player);

}
