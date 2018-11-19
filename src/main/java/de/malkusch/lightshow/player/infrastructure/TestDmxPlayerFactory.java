package de.malkusch.lightshow.player.infrastructure;

import de.malkusch.lightshow.player.DmxBus;
import de.malkusch.lightshow.player.model.DmxStream;

final class TestDmxPlayerFactory implements DmxPlayerFactory {

	private final int universe;
	private final int channels;
	private final DmxBus bus;

	TestDmxPlayerFactory(int universe, int channels, DmxBus bus) {
		this.universe = universe;
		this.channels = channels;
		this.bus = bus;
	}

	@Override
	public DmxPlayer build(DmxStream dmxStream) {
		return new TestDmxPlayer(universe, channels, bus);
	}

}
