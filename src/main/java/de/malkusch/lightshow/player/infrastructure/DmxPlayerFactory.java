package de.malkusch.lightshow.player.infrastructure;

import de.malkusch.lightshow.common.model.DmxStream;

final class DmxPlayerFactory {

	private final int universe;
	private final DmxBus bus;

	DmxPlayerFactory(int universe, DmxBus bus) {
		this.universe = universe;
		this.bus = bus;
	}

	public DmxPlayer build(DmxStream dmxStream) {
		return new DmxFramePlayer(universe, bus, dmxStream);
	}

}
