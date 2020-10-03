package blue.sparse.jst.specification.map.bsp.lumps.impl;

import blue.sparse.jst.specification.map.bsp.lumps.Lump;

public final class LumpBrushSide extends Lump {

	public short planeIndex;
	public short textureInfoIndex;
	public short displacementInfoIndex;
	public short bevel;

	@Override
	public String toString() {
		return "LumpBrushSide{" +
				"planeIndex=" + planeIndex +
				", textureInfoIndex=" + textureInfoIndex +
				", displacementInfoIndex=" + displacementInfoIndex +
				", bevel=" + bevel +
				'}';
	}
}
