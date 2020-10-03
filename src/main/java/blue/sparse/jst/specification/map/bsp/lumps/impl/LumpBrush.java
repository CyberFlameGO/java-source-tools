package blue.sparse.jst.specification.map.bsp.lumps.impl;

import blue.sparse.jst.specification.map.bsp.lumps.Lump;

public final class LumpBrush extends Lump {

	public int firstSideIndex;
	public int numSides;
	public int contents;

	@Override
	public String toString() {
		return "LumpBrush{" +
				"firstSideIndex=" + firstSideIndex +
				", numSides=" + numSides +
				", contents=" + contents +
				'}';
	}
}
