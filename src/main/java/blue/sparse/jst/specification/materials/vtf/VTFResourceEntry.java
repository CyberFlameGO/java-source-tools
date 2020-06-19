package blue.sparse.jst.specification.materials.vtf;

import blue.sparse.binary.annotation.LengthStatic;
import blue.sparse.binary.annotation.LittleEndian;

@LittleEndian
public final class VTFResourceEntry {
	@LengthStatic(3)
	public byte[] tag;
	public byte flags;
	public int offset;
}
