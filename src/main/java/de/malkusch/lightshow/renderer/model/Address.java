package de.malkusch.lightshow.renderer.model;

import java.util.Comparator;

public final class Address {

	static final Comparator<Address> COMPARATOR = Comparator.comparingInt(Address::toInt);

	private final int value;

	public Address(int address) {
		if (address < 0) {
			throw new IllegalArgumentException("Address must not be negative");
		}
		if (address >= 512) {
			throw new IllegalArgumentException("Address must be less than 512");
		}
		this.value = address;
	}

	public Address next() {
		return new Address(value + 1);
	}

	public int toInt() {
		return value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Address)) {
			return false;
		}
		var other = (Address) obj;
		return value == other.value;
	}

}