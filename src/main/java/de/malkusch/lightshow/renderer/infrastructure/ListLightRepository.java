package de.malkusch.lightshow.renderer.infrastructure;

import java.util.List;

import de.malkusch.lightshow.renderer.model.Light;
import de.malkusch.lightshow.renderer.model.LightRepository;

public final class ListLightRepository implements LightRepository {

	private final List<Light> lights;

	public ListLightRepository(List<Light> lights) {
		this.lights = lights;
	}

	@Override
	public List<Light> findAll() {
		return lights;
	}

}
