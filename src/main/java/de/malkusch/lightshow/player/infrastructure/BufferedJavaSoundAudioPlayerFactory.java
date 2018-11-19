package de.malkusch.lightshow.player.infrastructure;

import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import de.malkusch.lightshow.player.model.AudioStream;

final class BufferedJavaSoundAudioPlayerFactory implements AudioPlayerFactory {

	private final int bufferFrames;

	BufferedJavaSoundAudioPlayerFactory(int bufferFrames) {
		this.bufferFrames = bufferFrames;
	}

	@Override
	public AudioPlayer build(AudioStream stream) throws IOException {
		try {
			return new BufferedJavaSoundAudioPlayer(bufferFrames, stream);

		} catch (UnsupportedAudioFileException e) {
			throw new IllegalArgumentException(e);

		} catch (LineUnavailableException e) {
			throw new IOException(e);
		}
	}

}
