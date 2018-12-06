package de.malkusch.lightshow.renderer.model;

public final class Color {

	public Color(byte red, byte green, byte blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public Color(int red, int green, int blue) {
		this(toByte(red), toByte(green), toByte(blue));
	}

	static byte toByte(int value) {
		if (value < 0 || value > 255) {
			throw new IllegalArgumentException("Must be within 0 and 255");
		}
		return (byte) (value - 128);
	}

	private final byte red;

	public byte red() {
		return red;
	}

	private final byte green;

	public byte green() {
		return green;
	}

	private final byte blue;

	public byte blue() {
		return blue;
	}

	@Override
	public String toString() {
		return String.format("rgb(%d,%d,%d)", red, green, blue);
	}

	@Override
	public int hashCode() {
		return red + green + blue;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Color)) {
			return false;
		}
		var other = (Color) obj;
		return red == other.red && green == other.green && blue == other.blue;
	}
}
