package de.malkusch.lightshow.player.infrastructure;

import java.io.IOException;

public interface DmxFrameStream {

	int frameRate();

	default int channels() {
		return 512;
	}

	void readFrame(byte[] buffer) throws IOException;

	boolean hasFrames();

}
