package de.malkusch.lightshow.player;

public interface DmxBus {

	public void send(int universe, byte[] channels);

}
