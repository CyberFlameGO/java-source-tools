package blue.sparse.jst.specification.map.bsp.lumps.impl;

import blue.sparse.jst.specification.map.bsp.lumps.Lump;

public final class LumpLighting extends Lump {

	public byte red;
	public byte green;
	public byte blue;

	public byte exponent;

	@Override
	public String toString() {
		return "LumpLighting{" +
				"red=" + red +
				", green=" + green +
				", blue=" + blue +
				", exponent=" + exponent +
				'}';
	}
}
