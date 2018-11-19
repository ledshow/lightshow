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
		long nextFrame = lastFrame + rate.frameMilliseconds();
		lastFrame = time();
		long delay = nextFrame - time();
		if (delay <= 0) {
			return;
		}
		LOGGER.debug("Sleeping {} ms", delay);
		Thread.sleep(delay, 0);
	}

	private static long time() {
		return System.nanoTime() / 1000000;
	}

}
