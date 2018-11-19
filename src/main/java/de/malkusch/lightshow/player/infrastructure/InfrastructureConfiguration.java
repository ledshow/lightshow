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
		DmxPlayerFactory dmxPlayerFactory = new TestDmxPlayerFactory(0, 3, new ArtnetUnicastDmxBus("192.168.2.124"));
		return new SynchronizedPlayShowService(frameRate, audioPlayerFactory, dmxPlayerFactory);
	}

}
