package de.malkusch.lightshow.poc;

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

import ch.bildspur.artnet.ArtNetClient;

public final class SynchronizedAudioPoC {

	private static final Logger LOGGER = LoggerFactory.getLogger(SynchronizedAudioPoC.class);

	public static void main(String[] args) throws Exception {
		SynchronizedAudioPoC app = new SynchronizedAudioPoC();

		long start = System.currentTimeMillis();
		long lastTime = start;
		app.line.start();
		// app.playSound();
		while (true) {
			long realDelta = System.currentTimeMillis() - lastTime;
			long realTimestamp = System.currentTimeMillis() - start;
			lastTime = System.currentTimeMillis();
			long error = app.line.getMicrosecondPosition() / 1000 - realTimestamp;

			LOGGER.info("line frame: {}, line ms: {}, real ms: {}, error: {}, real delta ms: {}",
					app.line.getLongFramePosition(), app.line.getMicrosecondPosition() / 1000, realTimestamp, error,
					realDelta);

			app.render();
			Thread.sleep(16);
		}
	}

	public SynchronizedAudioPoC() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		initDmx();

		// clip = AudioSystem.getClip();
		audioStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/carneval.wav"));
		// clip.open(inputStream);

		format = audioStream.getFormat();
		int bufferSize = format.getFrameSize() * 5000;
		audioReadBuffer = new byte[bufferSize];
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format); // format is an
		line = (SourceDataLine) AudioSystem.getLine(info);
		line.open(format, bufferSize);
	}

	public void render() throws IOException, InterruptedException {
		updateAudioBuffer();
		renderDmx();
	}

	private final AudioFormat format;
	private final SourceDataLine line;
	private final AudioInputStream audioStream;
	private final byte[] audioReadBuffer;

	public void updateAudioBuffer() throws IOException {
		SourceDataLine line = (SourceDataLine) this.line;
		int length = line.available();
		if (length > audioReadBuffer.length) {
			length = audioReadBuffer.length;
		}

		audioStream.read(audioReadBuffer, 0, length);
		line.write(audioReadBuffer, 0, length);
	}

	private final ArtNetClient artnet = new ArtNetClient(null);
	private final byte[] dmxData = new byte[3];

	private void initDmx() {
		artnet.start();
	}

	public long timestamp() {
		return line.getMicrosecondPosition() / 1000;
	}

	public void renderDmx() throws InterruptedException {
		strobe();
		artnet.unicastDmx("192.168.2.124", 0, 0, dmxData); // TODO Discover nodes
		// artnet.broadcastDmx(0, 0, dmxData); // TODO Find a fast router
	}

	private void strobe() {
		int loopDuration = 200;
		long loopTimestamp = timestamp() % loopDuration;

		dmxData[0] = loopTimestamp < (loopDuration / 2) ? 0 : Byte.MAX_VALUE;
		dmxData[1] = dmxData[0];
		dmxData[2] = dmxData[0];
	}

	private void pulse() {
		int loopDuration = 500;
		long loopTimestamp = (line.getMicrosecondPosition() / 1000) % loopDuration;

		dmxData[0] = (byte) (255.0 / loopDuration * loopTimestamp);
		dmxData[1] = (byte) (dmxData[0] + 70);
		dmxData[2] = (byte) (dmxData[0] + 150);
	}

}
