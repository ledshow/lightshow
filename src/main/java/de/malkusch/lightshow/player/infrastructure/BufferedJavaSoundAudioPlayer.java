package de.malkusch.lightshow.player.infrastructure;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.malkusch.lightshow.player.model.AudioStream;

final class BufferedJavaSoundAudioPlayer implements AudioPlayer {

	private final AudioInputStream stream;
	private final SourceDataLine line;

	public BufferedJavaSoundAudioPlayer(int bufferFrames, AudioStream stream)
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		this.stream = AudioSystem.getAudioInputStream(stream.stream());
		AudioFormat format = this.stream.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		int bufferSize = format.getFrameSize() * bufferFrames;
		buffer = new byte[bufferSize];
		line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(format, bufferSize);
	}

	@Override
	public void startPlayback() {
		line.start();
	}

	private boolean hasFrames = true;

	@Override
	public boolean hasFrames() {
		return hasFrames;
	}

	private final byte[] buffer;

	@Override
	public void fillBuffer() throws IOException {
		int length = line.available();
		if (length > buffer.length) {
			length = buffer.length;
		}
		if (length == 0) {
			return;
		}

		int read = stream.read(buffer, 0, length);
		if (read == -1) {
			hasFrames = false;
			return;
		}

		int written = line.write(buffer, 0, length);
		if (written != read) {
			throw new IllegalStateException("Couldn't write what was available");
		}
	}

	@Override
	public long milliseconds() {
		return Math.round(line.getMicrosecondPosition() / 1000.0);
	}

	@Override
	public void stop() {
		line.stop();
	}

	@Override
	public void close() throws Exception {
		line.close();
	}

}
