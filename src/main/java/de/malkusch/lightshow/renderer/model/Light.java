package de.malkusch.lightshow.renderer.model;

import static java.util.Objects.requireNonNull;

public final class Light {

	public Light(LightId id, Address red, Address green, Address blue) {
		this.id = requireNonNull(id);
		this.red = requireNonNull(red);
		this.green = requireNonNull(green);
		this.blue = requireNonNull(blue);
	}

	private final LightId id;

	public LightId id() {
		return id;
	}

	private final Address red;

	public Address red() {
		return red;
	}

	private final Address green;

	public Address green() {
		return green;
	}

	private final Address blue;

	public Address blue() {
		return blue;
	}

	public Address[] addresses() {
		return new Address[] { red, green, blue };
	}

}
