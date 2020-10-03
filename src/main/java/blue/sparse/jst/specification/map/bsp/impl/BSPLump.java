package blue.sparse.jst.specification.map.bsp.impl;

import blue.sparse.binary.annotation.LengthStatic;

public class BSPLump {

	public int offset;
	public int length;
	public int version;

	@LengthStatic(4)
	public byte[] lumpIdentifier;

}
