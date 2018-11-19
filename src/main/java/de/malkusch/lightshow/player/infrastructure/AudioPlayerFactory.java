package de.malkusch.lightshow.player.infrastructure;

import java.io.IOException;

import de.malkusch.lightshow.player.model.AudioStream;

interface AudioPlayerFactory {

	AudioPlayer build(AudioStream audioStream) throws IOException;

}
