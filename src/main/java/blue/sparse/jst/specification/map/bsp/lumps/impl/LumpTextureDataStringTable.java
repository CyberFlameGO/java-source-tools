package blue.sparse.jst.specification.map.bsp.lumps.impl;

import blue.sparse.jst.specification.map.bsp.lumps.Lump;

public final class LumpTextureDataStringTable extends Lump {

	public int index;

	@Override
	public String toString() {
		return "LumpTextureDataStringTable{" +
				"offset=" + index +
				'}';
	}
}
