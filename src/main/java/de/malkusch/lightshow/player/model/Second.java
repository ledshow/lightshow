package de.malkusch.lightshow.player.model;

public final class Second {

	private final int value;

	public Second(int second) {
		if (second < 0) {
			throw new IllegalArgumentException("Second must not be negative");
		}
		this.value = second;
	}

	public int toInt() {
		return value;
	}

	@Override
	public String toString() {
		return String.format("%s s", value);
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Second)) {
			return false;
		}
		var other = (Second) obj;
		return value == other.value;
	}
}
