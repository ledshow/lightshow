package de.malkusch.lightshow.renderer.model;

public final class Color {

	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color RED = new Color(255, 0, 0);
	public static final Color ORANGE = new Color(255, 153, 0);
	public static final Color YELLOW = new Color(255, 255, 0);
	public static final Color GREEN = new Color(0, 255, 0);
	public static final Color BLUE = new Color(0, 0, 255);
	public static final Color PURPLE = new Color(102, 0, 102);
	public static final Color PINK = new Color(255, 0, 255);
	public static final Color AQUA = new Color(0, 255, 255);
	public static final Color AQUAMARINE = new Color(255 / 2, 255, (int) (255 * 0.83));

	public Color(int red, int green, int blue) {
		this.red = assertByte(red);
		this.green = assertByte(green);
		this.blue = assertByte(blue);
	}

	static int assertByte(int value) {
		if (value < 0 || value > 255) {
			throw new IllegalArgumentException("Must be within 0 and 255");
		}
		return value;
	}

	private final int red;

	public int red() {
		return red;
	}

	private final int green;

	public int green() {
		return green;
	}

	private final int blue;

	public int blue() {
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
