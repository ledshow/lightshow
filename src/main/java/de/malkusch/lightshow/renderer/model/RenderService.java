package de.malkusch.lightshow.renderer.model;

import static java.util.Objects.requireNonNull;

public final class RenderService {

	private final LightRepository lights;

	public RenderService(LightRepository lights, MixService mixer) {
		this.lights = lights;
		this.mixer = mixer;
	}

	public byte[] render(Show show, Position position) {
		requireNonNull(show);
		requireNonNull(position);
		if (position.isAfter(show.end())) {
			throw new IllegalArgumentException("Must be within show");
		}

		var frame = new byte[lights.maxAddress().toInt()];
		show.lightIds().forEach(it -> render(frame, show, position, it));
		return frame;
	}

	private final MixService mixer;

	private void render(byte[] frame, Show show, Position position, LightId lightId) {
		var light = lights.find(lightId);
		var colors = show.transformations(lightId, position).map(it -> it.transform(position));
		var color = mixer.mix(colors);
		frame[light.red().toInt()] = color.red();
		frame[light.green().toInt()] = color.green();
		frame[light.blue().toInt()] = color.blue();
	}

}
