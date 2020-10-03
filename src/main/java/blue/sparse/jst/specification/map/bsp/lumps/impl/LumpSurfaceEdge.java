package blue.sparse.jst.specification.map.bsp.lumps.impl;

import blue.sparse.jst.specification.map.bsp.lumps.Lump;

public final class LumpSurfaceEdge extends Lump {

	public int edgeIndex;

	@Override
	public String toString() {
		return "LumpSurfaceEdge{" +
				"edgeIndex=" + edgeIndex +
				'}';
	}
}
