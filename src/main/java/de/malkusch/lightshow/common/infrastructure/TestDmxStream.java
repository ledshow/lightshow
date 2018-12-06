package de.malkusch.lightshow.common.infrastructure;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.malkusch.lightshow.common.model.DmxStream;
import de.malkusch.lightshow.common.model.FrameRate;

public final class TestDmxStream implements DmxStream {

	private final FrameRate frameRate = new FrameRate(120);
	private final int channels;
	private final int frequency;
	private static final Logger LOGGER = LoggerFactory.getLogger(TestDmxStream.class);

	public TestDmxStream(int channels, int frequency) {
		this.channels = channels;
		this.frequency = frequency;
	}

	@Override
	public int channels() {
		return channels;
	}

	@Override
	public FrameRate frameRate() {
		return frameRate;
	}

	private long position;

	@Override
	public void readFrame(byte[] buffer) throws IOException {
		int loopFrameCount = frameRate.framesPerSecond() / frequency;
		int loopPosition = (int) (position % loopFrameCount);
		byte value = (loopPosition < loopFrameCount / 2) ? 0 : Byte.MIN_VALUE;
		LOGGER.debug("test value: {}", value);
		position++;

		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = value;
		}
	}

	@Override
	public boolean hasFrames() {
		return true;
	}

	@Override
	public void close() {
	}

}
