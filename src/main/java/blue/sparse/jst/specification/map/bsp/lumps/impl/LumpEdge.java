package blue.sparse.jst.specification.map.bsp.lumps.impl;

import blue.sparse.jst.specification.map.bsp.lumps.Lump;

public final class LumpEdge extends Lump {

	public short a;
	public short b;

	@Override
	public String toString() {
		return "LumpEdge{" +
				"a=" + a +
				", b=" + b +
				'}';
	}
}
