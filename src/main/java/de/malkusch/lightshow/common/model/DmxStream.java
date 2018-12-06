package de.malkusch.lightshow.common.model;

import java.io.IOException;

public interface DmxStream extends AutoCloseable {

	FrameRate frameRate();

	default int channels() {
		return 512;
	}

	void readFrame(byte[] buffer) throws IOException;

	boolean hasFrames();

}
