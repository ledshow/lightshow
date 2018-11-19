package de.malkusch.lightshow.player.infrastructure;

public interface DmxBus extends AutoCloseable {

	public void send(int universe, byte[] channels);

}
