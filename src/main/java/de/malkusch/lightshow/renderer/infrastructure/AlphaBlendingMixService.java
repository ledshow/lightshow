package de.malkusch.lightshow.renderer.infrastructure;

import static de.malkusch.lightshow.renderer.model.AlphaColor.NONE;
import static de.malkusch.lightshow.renderer.model.Color.BLACK;
import static org.apache.commons.math3.linear.MatrixUtils.createRealVector;

import java.util.stream.Stream;

import org.apache.commons.math3.linear.RealVector;

import de.malkusch.lightshow.renderer.model.AlphaColor;
import de.malkusch.lightshow.renderer.model.Color;
import de.malkusch.lightshow.renderer.model.MixService;

public final class AlphaBlendingMixService implements MixService {

	@Override
	public Color mix(Stream<AlphaColor> colors) {
		AlphaColor mixed = colors.reduce(NONE, AlphaBlendingMixService::mix);
		return mix(mixed, BLACK);
	}

	private static Color mix(AlphaColor color, Color background) {
		var alpha = real(color.alpha());
		var a = vector(color.color());
		var b = vector(background);
		var c = a.mapMultiply(alpha).add(b.mapMultiply(1 - alpha));
		return color(c);
	}

	private static AlphaColor mix(AlphaColor colorB, AlphaColor colorA) {
		var alphaA = real(colorA.alpha());
		var alphaB = real(colorB.alpha());
		var a = vector(colorA.color());
		var b = vector(colorB.color());

		var alpha = alphaA + (1 - alphaA) * alphaB;
		var c = a.mapMultiply(alphaA).add(b.mapMultiply((1 - alphaA) * alphaB)).mapDivide(alpha);

		return new AlphaColor(color(c), toInt(alpha));
	}

	private static Color color(RealVector vector) {
		return new Color(toInt(vector.getEntry(0)), toInt(vector.getEntry(1)), toInt(vector.getEntry(2)));
	}

	private static RealVector vector(Color color) {
		return createRealVector(new double[] { real(color.red()), real(color.green()), real(color.blue()) });
	}

	private static double real(int value) {
		return value / (double) 255;
	}

	private static int toInt(double real) {
		return (int) (real * 255);
	}

}
