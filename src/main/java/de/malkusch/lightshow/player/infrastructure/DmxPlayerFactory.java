package de.malkusch.lightshow.player.infrastructure;

import de.malkusch.lightshow.player.model.DmxStream;

interface DmxPlayerFactory {

	DmxPlayer build(DmxStream dmxStream);

}
