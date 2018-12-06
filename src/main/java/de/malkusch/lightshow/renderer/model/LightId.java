package de.malkusch.lightshow.renderer.model;

import static java.util.Objects.requireNonNull;

public final class LightId {

	private final String value;

	public LightId(String value) {
		this.value = requireNonNull(value);
		if (value.isBlank()) {
			throw new IllegalArgumentException("Must not be blank");
		}
	}

	@Override
	public String toString() {
		return value;
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LightId)) {
			return false;
		}
		var other = (LightId) obj;
		return value.equals(other.value);
	}

}
