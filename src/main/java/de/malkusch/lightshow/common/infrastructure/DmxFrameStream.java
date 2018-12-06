package de.malkusch.lightshow.common.infrastructure;

import java.io.IOException;

public interface DmxFrameStream {

	int frameRate();

	default int channels() {
		return 512;
	}

	void readFrame(byte[] buffer) throws IOException;

	boolean hasFrames();

}
