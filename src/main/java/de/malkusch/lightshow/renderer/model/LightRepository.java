package de.malkusch.lightshow.renderer.model;

import java.util.Arrays;
import java.util.List;

public interface LightRepository {

	Light find(LightId id);

	List<Light> findAll();

	default Address maxAddress() {
		return findAll().stream().flatMap(it -> Arrays.stream(it.addresses())).max(Address.COMPARATOR).get();
	}

}
