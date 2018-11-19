package de.malkusch.lightshow.player.infrastructure;

import java.io.IOException;

public interface DmxFrameStream {

	int frameRate();

	void readFrame(byte[] buffer) throws IOException;

	boolean hasFrames();

}
