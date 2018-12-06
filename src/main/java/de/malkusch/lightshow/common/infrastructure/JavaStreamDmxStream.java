package de.malkusch.lightshow.common.infrastructure;

import java.io.IOException;
import java.util.stream.Stream;

import de.malkusch.lightshow.common.model.DmxStream;

public final class JavaStreamDmxStream implements DmxStream {

	public JavaStreamDmxStream(Stream<byte[]> frames) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int frameRate() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void readFrame(byte[] buffer) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasFrames() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
	}

}
