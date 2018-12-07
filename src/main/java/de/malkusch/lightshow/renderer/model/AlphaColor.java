package de.malkusch.lightshow.renderer.model;

import static java.util.Objects.requireNonNull;

public final class AlphaColor {

	public AlphaColor(Color color, int alpha) {
		this.color = requireNonNull(color);
		this.alpha = Color.assertByte(alpha);
	}

	private final int alpha;

	public int alpha() {
		return alpha;
	}

	private final Color color;

	public Color color() {
		return color;
	}

	@Override
	public String toString() {
		return String.format("%s alpha(%d)", color, alpha);
	}

	@Override
	public int hashCode() {
		return color.hashCode() + alpha;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AlphaColor)) {
			return false;
		}
		var other = (AlphaColor) obj;
		return color.equals(other.color) && alpha == other.alpha;
	}
}
