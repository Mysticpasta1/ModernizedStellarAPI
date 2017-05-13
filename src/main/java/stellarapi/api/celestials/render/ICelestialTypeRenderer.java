package stellarapi.api.celestials.render;

import stellarapi.api.celestials.CelestialCollection;

/**
 * Renderer capable of rendering specific celestial type of objects.
 * */
public interface ICelestialTypeRenderer {

	public void upload(CelestialCollection collection,
			ICelestialVertexBuffer buffer);

}