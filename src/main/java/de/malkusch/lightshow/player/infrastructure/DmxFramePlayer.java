package de.malkusch.lightshow.player.infrastructure;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.malkusch.lightshow.common.model.DmxStream;

final class DmxFramePlayer implements DmxPlayer, AutoCloseable {

	private final DmxStream stream;
	private final DmxBus bus;
	private final int universe;
	private static final Logger LOGGER = LoggerFactory.getLogger(DmxFramePlayer.class);

	DmxFramePlayer(int universe, DmxBus bus, DmxStream stream) {
		this.universe = universe;
		this.bus = bus;
		this.stream = stream;
		currentFrame = new byte[stream.channels()];
	}

	@Override
	public void startPlayback() {
	}

	@Override
	public boolean hasFrames() {
		return stream.hasFrames();
	}

	@Override
	public void stop() {
	}

	private final byte[] currentFrame;
	private long framePosition;

	@Override
	public void render(long milliseconds) throws IOException {
		long renderPosition = millisecondsToFrame(milliseconds);
		LOGGER.debug("framePosition: {}, renderPosition: {}", framePosition, renderPosition);

		while (renderPosition > framePosition && stream.hasFrames()) {
			stream.readFrame(currentFrame);
			framePosition++;
			LOGGER.debug("read frame: {}", framePosition);
		}

		LOGGER.debug("render {}: {}", framePosition, currentFrame);
		bus.send(universe, currentFrame);
	}

	private long millisecondsToFrame(long milliseconds) {
		return Math.round(milliseconds * stream.frameRate().framesPerSecond() / 1000.0);
	}

	@Override
	public void close() throws Exception {
		bus.close();
	}

}
