package blue.sparse.jst.specification.models.vvd;

import blue.sparse.binary.annotation.LengthStatic;

public final class VVDBoneWeight {
	@LengthStatic(3)
	public float[] weight;
	@LengthStatic(3)
	public byte[] bone;
	public byte numbones;
}
