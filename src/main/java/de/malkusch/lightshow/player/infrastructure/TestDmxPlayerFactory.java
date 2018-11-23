package de.malkusch.lightshow.player.infrastructure;

import de.malkusch.lightshow.player.model.DmxStream;

final class TestDmxPlayerFactory implements DmxPlayerFactory {

	private final int universe;
	private final DmxBus bus;
	private final int frequency;

	TestDmxPlayerFactory(int frequency, int universe, DmxBus bus) {
		this.frequency = frequency;
		this.universe = universe;
		this.bus = bus;
	}

	@Override
	public DmxPlayer build(DmxStream dmxStream) {
		DmxTestFrameStream testStream = new DmxTestFrameStream(3, frequency);
		return new DmxFramePlayer(universe, bus, testStream);
	}

}
