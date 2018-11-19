package de.malkusch.lightshow.player.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class FrameRateEngine {

	private final FrameRate rate;
	private long lastFrame = time();
	private final static Logger LOGGER = LoggerFactory.getLogger(FrameRateEngine.class);

	FrameRateEngine(FrameRate rate) {
		this.rate = rate;
	}

	public void sleep() throws InterruptedException {
		LOGGER.debug("last: {}", lastFrame / 1000000);
		long nextFrame = lastFrame + rate.frameMilliseconds() * 1000000;
		long now = time();
		lastFrame = nextFrame;
		long delay = (nextFrame - now) / 1000000;
		if (delay <= 0) {
			LOGGER.debug("now: {}, delay: {}, next: {}", now, delay, nextFrame);
			LOGGER.warn("Too slow. Consider decreasing the frame rate.");
			return;
		}
		LOGGER.debug("sleeping {} ms", delay);
		Thread.sleep(delay, 0);
	}

	private static long time() {
		return System.nanoTime();
	}

}
