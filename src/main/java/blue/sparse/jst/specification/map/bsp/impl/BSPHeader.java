package blue.sparse.jst.specification.map.bsp.impl;

import blue.sparse.binary.annotation.LengthStatic;
import blue.sparse.binary.annotation.LittleEndian;

@LittleEndian
public class BSPHeader {
	public int identifier;
	public int version;

	@LengthStatic(64)
	public BSPLump[] lumps;
	public int mapRevision;
}
