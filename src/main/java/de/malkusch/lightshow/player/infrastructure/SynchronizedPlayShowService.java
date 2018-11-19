package de.malkusch.lightshow.player.infrastructure;

import static java.util.Objects.requireNonNull;

import java.io.IOException;

import de.malkusch.lightshow.player.model.PlayShowService;
import de.malkusch.lightshow.player.model.Show;

final class SynchronizedPlayShowService implements PlayShowService {

	private final AudioPlayerFactory audioPlayerFactory;
	private final DmxPlayerFactory dmxPlayerFactory;
	private final FrameRate frameRate;

	SynchronizedPlayShowService(FrameRate frameRate, AudioPlayerFactory audioPlayerFactory,
			DmxPlayerFactory dmxPlayerFactory) {

		this.frameRate = frameRate;
		this.audioPlayerFactory = audioPlayerFactory;
		this.dmxPlayerFactory = dmxPlayerFactory;
	}

	@Override
	public void play(Show show) throws InterruptedException, IOException {
		requireNonNull(show);

		try (AudioPlayer audioPlayer = audioPlayerFactory.build(show.audioStream())) {
			DmxPlayer dmxPlayer = dmxPlayerFactory.build(show.dmxStream());

			audioPlayer.startPlayback();
			dmxPlayer.startPlayback();
			FrameRateEngine frameRateEngine = new FrameRateEngine(frameRate);

			while (audioPlayer.hasFrames() || dmxPlayer.hasFrames()) {
				if (Thread.interrupted()) {
					throw new InterruptedException("Playback was interrupted");
				}

				long milliseconds = audioPlayer.milliseconds();
				dmxPlayer.synchronizeMilliseconds(milliseconds);

				frameRateEngine.sleep();
			}

		} catch (InterruptedException | IOException e) {
			throw e;

		} catch (Exception e) {
			throw new IOException(e);
		}
	}

}
