package blue.sparse.jst.specification.map.bsp.lumps.impl;

import blue.sparse.jst.specification.map.bsp.lumps.Lump;
import org.joml.Vector3f;

public final class LumpVertex extends Lump {

	public Vector3f vector;

	@Override
	public String toString() {
		return "LumpVertex{" +
				"vector=" + vector +
				'}';
	}
}
