package de.malkusch.lightshow.common.infrastructure;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;

import de.malkusch.lightshow.common.model.DmxStream;
import de.malkusch.lightshow.common.model.FrameRate;

public final class JavaStreamDmxStream implements DmxStream {

	public JavaStreamDmxStream(FrameRate rate, Stream<byte[]> frames) {
		this.frames = requireNonNull(frames);
		this.rate = requireNonNull(rate);
		this.iterator = frames.iterator();
	}

	private final FrameRate rate;

	@Override
	public FrameRate frameRate() {
		return rate;
	}

	private final Iterator<byte[]> iterator;

	@Override
	public void readFrame(byte[] buffer) throws IOException {
		var frame = iterator.next();
		for (int i = 0; i < buffer.length; i++) {
			buffer[i] = i < frame.length ? frame[i] : 0;
		}
	}

	@Override
	public boolean hasFrames() {
		return iterator.hasNext();
	}

	private final Stream<byte[]> frames;

	@Override
	public void close() {
		frames.close();
	}

}
