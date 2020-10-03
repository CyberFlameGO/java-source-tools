package blue.sparse.jst.specification.map.bsp.lumps.impl;

import blue.sparse.jst.specification.map.bsp.lumps.Lump;
import org.joml.Vector3f;

public final class LumpPlane extends Lump {

	public Vector3f normal;
	public float distance;
	public int type;

	@Override
	public String toString() {
		return "LumpPlane{" +
				"normal=" + normal +
				", distance=" + distance +
				", type=" + type +
				'}';
	}
}
