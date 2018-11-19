package de.malkusch.lightshow.player.infrastructure;

import de.malkusch.lightshow.player.DmxBus;

final class TestDmxPlayer implements DmxPlayer {

	private final byte[] channels;
	private final int universe;
	private final DmxBus bus;

	public TestDmxPlayer(int universe, int channels, DmxBus bus) {
		this.universe = universe;
		this.channels = new byte[channels];
		this.bus = bus;
	}

	@Override
	public void startPlayback() {
	}

	@Override
	public boolean hasFrames() {
		return true;
	}

	@Override
	public void stop() {
	}

	private static final int loopDuration = 100;

	@Override
	public void synchronizeMilliseconds(long milliseconds) {
		long loopTimestamp = milliseconds % loopDuration;
		byte value = loopTimestamp < (loopDuration / 2) ? 0 : Byte.MAX_VALUE;
		for (int i = 0; i < channels.length; i++) {
			channels[i] = value;
		}

		bus.send(universe, channels);
	}

}
