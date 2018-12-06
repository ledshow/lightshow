package de.malkusch.lightshow.player.infrastructure;

import de.malkusch.lightshow.player.model.PlayShowService;

public final class InfrastructureConfiguration {

	private final AudioPlayerFactory audioPlayerFactory;
	private final FrameRate frameRate;

	public InfrastructureConfiguration(int bufferFrames, FrameRate frameRate) {
		audioPlayerFactory = new BufferedJavaSoundAudioPlayerFactory(bufferFrames);
		this.frameRate = frameRate;
	}

	public PlayShowService playShowService() {
		int testFrequency = 10;
		int universe = 0;
		// DmxBus bus = new ArtnetUnicastDmxBus("192.168.2.124");
		DmxBus bus = new ArtnetUnicastDmxBus("10.0.0.60");
		DmxPlayerFactory dmxPlayerFactory = new TestDmxPlayerFactory(testFrequency, universe, bus);
		Synchronizer synchronizer = new NanoSynchronizer();
		return new SynchronizedPlayShowService(frameRate, audioPlayerFactory, dmxPlayerFactory, synchronizer);
	}

}
