package de.malkusch.lightshow.player.infrastructure;

import ch.bildspur.artnet.ArtNetClient;

final class ArtnetUnicastDmxBus implements DmxBus {

	private final String address;
	private final ArtNetClient artnet = new ArtNetClient(null);

	ArtnetUnicastDmxBus(String address) {
		this.address = address; // TODO Discover nodes
		artnet.start();
	}

	@Override
	public void send(int universe, byte[] channels) {
		artnet.unicastDmx(address, 0, universe, channels);
		// artnet.broadcastDmx(0, 0, dmxData); // TODO Find a fast router
	}

	@Override
	public void close() throws Exception {
		artnet.stop();
	}

}
