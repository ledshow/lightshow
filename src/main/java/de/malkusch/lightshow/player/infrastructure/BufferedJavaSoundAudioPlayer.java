package de.malkusch.lightshow.player.infrastructure;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.malkusch.lightshow.player.model.AudioStream;

final class BufferedJavaSoundAudioPlayer implements AudioPlayer {

	private final AudioInputStream stream;
	private final SourceDataLine line;
	private final Thread reader;
	private final Logger LOGGER = LoggerFactory.getLogger(BufferedJavaSoundAudioPlayer.class);

	public BufferedJavaSoundAudioPlayer(int bufferFrames, AudioStream stream)
			throws UnsupportedAudioFileException, IOException, LineUnavailableException {

		this.stream = AudioSystem.getAudioInputStream(stream.stream());
		AudioFormat format = this.stream.getFormat();
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
		int bufferSize = format.getFrameSize() * bufferFrames;
		line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(format, bufferSize);

		reader = new Thread(this::fillBuffer, "audio");
	}

	@Override
	public void startPlayback() {
		line.start();
		reader.start();
	}

	private volatile boolean hasFrames = true;

	@Override
	public boolean hasFrames() {
		return hasFrames;
	}

	private void fillBuffer() {
		try {
			byte[] buffer = new byte[line.getBufferSize() * 5];
			int read;
			while (hasFrames && (read = stream.read(buffer)) != -1) {
				LOGGER.debug("read {} bytes", read);
				int offset = 0;
				int length = read;
				int written = 0;
				while (hasFrames && length > 0) {
					if (Thread.interrupted()) {
						return;
					}
					written = line.write(buffer, offset, length);
					LOGGER.debug("wrote {} bytes", written);
					offset += written;
					length -= offset;
				}
			}

		} catch (IOException e) {
			LOGGER.error("Failed reading audio file", e);

		} finally {
			hasFrames = false;
		}
	}

	@Override
	public long milliseconds() {
		long us = line.getMicrosecondPosition();
		LOGGER.debug("µs: {}", us);
		return Math.round(us / 1000.0);
	}

	@Override
	public void stop() {
		hasFrames = false;
		line.stop();
	}

	@Override
	public void close() throws Exception {
		hasFrames = false;
		line.close();
	}

}
