package de.malkusch.lightshow.renderer.model;

import java.util.Arrays;
import java.util.List;

public interface LightRepository {

	List<Light> findAll();

	default Light find(LightId id) {
		return findAll().stream().filter(it -> it.id().equals(id)).findFirst().get();
	}

	default Address maxAddress() {
		return findAll().stream().flatMap(it -> Arrays.stream(it.addresses())).max(Address.COMPARATOR).get();
	}

}
