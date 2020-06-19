package blue.sparse.jst.specification.models.mdl;

import blue.sparse.binary.annotation.LengthStatic;

public final class MDLTextureData {
	public int nameOffset;
	public int flags;
	public int used;
	public int unused;
	public int material;
	public int clientMaterial;

	@LengthStatic(10)
	public int[] unused2;
}
